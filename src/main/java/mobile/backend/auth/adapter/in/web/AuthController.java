package mobile.backend.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mobile.backend.auth.adapter.in.web.request.RefreshTokenRequest;
import mobile.backend.auth.adapter.in.web.request.SocialLoginRequest;
import mobile.backend.auth.adapter.in.web.response.LogoutResponse;
import mobile.backend.auth.adapter.in.web.response.RefreshTokenResponse;
import mobile.backend.auth.adapter.in.web.response.SocialLoginResponse;
import mobile.backend.auth.application.port.in.LogoutUseCase;
import mobile.backend.auth.application.port.in.RefreshTokenUseCase;
import mobile.backend.auth.application.port.in.SocialLoginUseCase;
import mobile.backend.auth.domain.model.TokenPair;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.global.security.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SocialLoginUseCase socialLoginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @Operation(summary = "소셜 로그인", description = "Google 또는 Kakao 소셜 로그인을 처리합니다.")
    @PostMapping("/social-login")
    public ResponseEntity<BaseResponse<SocialLoginResponse>> socialLogin(
            @RequestBody SocialLoginRequest request) {

        TokenPair tokenPair = socialLoginUseCase.execute(request.toCommand());

        // userId는 accessToken에서 추출 (AuthService에서 생성한 토큰 기반)
        // 실제로는 tokenPair에 userId를 포함하거나 별도로 반환 필요
        // 여기서는 임시로 처리 - AuthService 수정 필요할 수 있음

        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", tokenPair.getAccessToken());

        SocialLoginResponse response = SocialLoginResponse.of(
                tokenPair.getUserId(),
                tokenPair.getRefreshToken()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(BaseResponse.success(response));
    }

    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<RefreshTokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        String newAccessToken = refreshTokenUseCase.execute(request.toCommand());

        RefreshTokenResponse response = RefreshTokenResponse.of(newAccessToken);

        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다. Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<LogoutResponse>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        logoutUseCase.execute(userDetails.getUserId());

        LogoutResponse response = LogoutResponse.of("Logout success");

        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
