package mobile.backend.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.application.port.in.AuthCommandUseCase;
import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import mobile.backend.auth.application.port.out.SocialTokenValidator;
import mobile.backend.auth.domain.command.RefreshTokenCommand;
import mobile.backend.auth.domain.command.SocialLoginCommand;
import mobile.backend.auth.domain.model.RefreshToken;
import mobile.backend.auth.domain.model.AuthToken;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;
import mobile.backend.global.security.jwt.JwtProperties;
import mobile.backend.global.security.jwt.JwtProvider;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import mobile.backend.user.domain.model.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import mobile.backend.user.domain.model.SocialUserInfo;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements AuthCommandUseCase {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final List<SocialTokenValidator> socialTokenValidators;

    @Override
    @Transactional
    public AuthToken login(SocialLoginCommand command) {
        // 1. 소셜 토큰 검증 및 소셜 ID 추출
        SocialUserInfo userInfo = validateSocialToken(command.getSocialToken(), command.getProvider());

        // 2. DB에서 사용자 조회 또는 생성
        User user = userRepository
                .findBySocialIdAndSocialType(userInfo.getSocialId(), command.getProvider())
                .orElseGet(() -> userRepository.save(User.fromSocialUserInfo(userInfo, command.getProvider())));

        // 3. JWT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // 4. RefreshToken 저장 (기존 토큰이 있으면 덮어쓰기)
        refreshTokenRepository.save(
                RefreshToken.create(user.getId(), refreshToken, jwtProperties.getRefreshTokenExpireTime())
        );
        return AuthToken.of(user.getId(), accessToken, refreshToken);

    }

    @Override
    public String refreshAccessToken(RefreshTokenCommand command) {
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
    public void logout(Long userId) {
        // RefreshToken 삭제
        refreshTokenRepository.deleteByUserId(userId);
        // RefreshTokenRepository에서 AuthErrorCode.REFRESH_TOKEN_DELETE_FAILED로 예외 처리됨
    }


    private SocialUserInfo validateSocialToken(String socialToken, SocialType socialType) {
        for (SocialTokenValidator validator : socialTokenValidators) {
            if (validator.matchesSocialType(socialType)) {
                return validator.validateAndGetUserInfo(socialToken);
            }
        }
        throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
    }

}
