package mobile.backend.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.application.port.in.TestAuthCommandUseCase;
import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import mobile.backend.auth.domain.command.TestSignupCommand;
import mobile.backend.auth.domain.command.TestLoginCommand;
import mobile.backend.auth.domain.model.AuthToken;
import mobile.backend.auth.domain.model.RefreshToken;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;
import mobile.backend.global.security.jwt.JwtProperties;
import mobile.backend.global.security.jwt.JwtProvider;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.SocialType;
import mobile.backend.user.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestAuthService implements TestAuthCommandUseCase {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User signup(TestSignupCommand command) {
        // 1. 이메일 중복 체크
        if (userRepository.existsBySocialIdAndSocialType(command.getEmail(), SocialType.TEST)) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        // 3. 테스트 사용자 생성 및 저장 (DB 예외는 Repository에서 처리)
        User user = User.createTestUser(command.getEmail(), encodedPassword, command.getName());
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Override
    @Transactional
    public AuthToken login(TestLoginCommand command) {
        // 1. 이메일로 사용자 조회 (비즈니스 로직 - Service에서 처리)
        User user = userRepository
                .findBySocialIdAndSocialType(command.getEmail(), SocialType.TEST)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_CREDENTIALS));

        // 2. 비밀번호 검증 (비즈니스 로직 - Service에서 처리)
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        // 3. JWT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // 4. RefreshToken 저장 (기존 토큰 덮어쓰기)
        refreshTokenRepository.save(
                RefreshToken.create(user.getId(), refreshToken, jwtProperties.getRefreshTokenExpireTime())
        );

        return AuthToken.of(user.getId(), accessToken, refreshToken);
    }
}
