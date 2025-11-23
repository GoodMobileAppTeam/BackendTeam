package mobile.backend.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.application.port.in.LogoutUseCase;
import mobile.backend.auth.application.port.in.RefreshTokenUseCase;
import mobile.backend.auth.application.port.in.SocialLoginUseCase;
import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import mobile.backend.auth.application.port.out.SocialTokenValidator;
import mobile.backend.auth.domain.command.RefreshTokenCommand;
import mobile.backend.auth.domain.command.SocialLoginCommand;
import mobile.backend.auth.domain.model.RefreshToken;
import mobile.backend.auth.domain.model.TokenPair;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;
import mobile.backend.global.security.jwt.JwtProperties;
import mobile.backend.global.security.jwt.JwtProvider;
import mobile.backend.user.adapter.out.persistence.entity.UserEntity;
import mobile.backend.user.adapter.out.persistence.jpa.UserJpaRepository;
import mobile.backend.user.domain.model.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements SocialLoginUseCase, RefreshTokenUseCase, LogoutUseCase {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserJpaRepository userJpaRepository;
    private final List<SocialTokenValidator> socialTokenValidators;

    @Override
    @Transactional
    public TokenPair execute(SocialLoginCommand command) {
        // 파라미터 검증
        if (command.getSocialToken() == null || command.getSocialId() == null || command.getProvider() == null) {
            throw new CustomException(AuthErrorCode.INVALID_PARAMETER);
        }

        // 1. 소셜 토큰 검증 및 소셜 ID 추출
        String validatedSocialId = validateSocialToken(command.getSocialToken(), command.getProvider());

        // 2. 클라이언트가 보낸 socialId와 검증된 socialId 비교
        if (!validatedSocialId.equals(command.getSocialId())) {
            throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
        }

        // 3. DB에서 사용자 조회 또는 생성
        UserEntity user = userJpaRepository
                .findBySocialIdAndSocialType(command.getSocialId(), command.getProvider())
                .orElseGet(() -> createNewUser(command.getSocialId(), command.getProvider()));

        // 4. JWT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // 5. RefreshToken 저장 (기존 토큰이 있으면 덮어쓰기)
        saveRefreshToken(user.getId(), refreshToken);

        return TokenPair.of(user.getId(), accessToken, refreshToken);
    }

    @Override
    public String execute(RefreshTokenCommand command) {
        // 파라미터 검증
        if (command.getRefreshToken() == null || command.getRefreshToken().isEmpty()) {
            throw new CustomException(AuthErrorCode.INVALID_PARAMETER);
        }

        // 1. RefreshToken 검증
        if (!jwtProvider.validateToken(command.getRefreshToken())) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. RefreshToken에서 userId 추출
        Long userId = jwtProvider.getUserIdFromToken(command.getRefreshToken());

        // 3. Redis에서 RefreshToken 조회
        RefreshToken storedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        // 4. 저장된 토큰과 요청된 토큰 비교
        if (!storedToken.getToken().equals(command.getRefreshToken())) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 5. 만료 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.deleteByUserId(userId);
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 6. 새로운 AccessToken 발급
        return jwtProvider.generateAccessToken(userId);
    }

    @Override
    @Transactional
    public void execute(Long userId) {
        try {
            // RefreshToken 삭제
            refreshTokenRepository.deleteByUserId(userId);
        } catch (Exception e) {
            log.error("Logout failed for user {}: {}", userId, e.getMessage());
            throw new CustomException(AuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String validateSocialToken(String socialToken, SocialType provider) {
        try {
            return socialTokenValidators.stream()
                    .filter(validator -> validator.supports(provider))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN))
                    .validateAndGetSocialId(socialToken);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Social token validation failed: {}", e.getMessage());
            throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
        }
    }


    private UserEntity createNewUser(String socialId, SocialType socialType) {
        try {
            UserEntity newUser = UserEntity.builder()
                    .socialId(socialId)
                    .socialType(socialType)
                    .name("User_" + socialId.substring(0, Math.min(8, socialId.length())))
                    .profileImageUrl(null)
                    .id2(0)
                    .createdAt(LocalDateTime.now())
                    .build();
            return userJpaRepository.save(newUser);
        } catch (Exception e) {
            log.error("User creation failed: {}", e.getMessage(), e);
            throw new CustomException(AuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveRefreshToken(Long userId, String refreshToken) {
        try {
            LocalDateTime expiresAt = LocalDateTime.now()
                    .plusSeconds(jwtProperties.getRefreshTokenExpireTime() / 1000);

            RefreshToken token = RefreshToken.of(
                    userId,
                    refreshToken,
                    expiresAt
            );

            refreshTokenRepository.save(token);
        } catch (Exception e) {
            log.error("RefreshToken save failed: {}", e.getMessage());
            throw new CustomException(AuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
