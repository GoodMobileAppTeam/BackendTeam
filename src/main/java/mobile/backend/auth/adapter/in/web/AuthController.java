package mobile.backend.auth.adapter.in.web;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mobile.backend.auth.adapter.in.web.request.SocialLoginRequest;
import mobile.backend.auth.adapter.in.web.response.RefreshTokenResponse;
import mobile.backend.auth.adapter.in.web.response.SocialLoginResponse;
import mobile.backend.auth.application.port.in.AuthCommandUseCase;
import mobile.backend.auth.domain.command.RefreshTokenCommand;
import mobile.backend.auth.domain.model.AuthToken;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.global.security.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;


@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandUseCase authCommandUseCase;

    @Operation(summary = "소셜 로그인", description = "Google 또는 Kakao 소셜 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<SocialLoginResponse>> socialLogin(
            @Valid @RequestBody SocialLoginRequest request,
            HttpServletResponse httpResponse) {

        AuthToken authToken = authCommandUseCase.login(request.toCommand());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authToken.getRefreshToken())
                .httpOnly(true)  // JavaScript 접근 차단
                .secure(false)  // HTTPS에서만 전송 (운영 환경) 로컬 테스트 시 false로 변경
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 7일 (초 단위)
                .sameSite("Strict")  // CSRF 방어
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        SocialLoginResponse response = SocialLoginResponse.of(
                authToken.getUserId(),
                authToken.getAccessToken());

        return ResponseEntity.ok()
                .body(BaseResponse.success(response));
    }

    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<RefreshTokenResponse>> refreshToken(
            @CookieValue(name = "refreshToken", required = true) String refreshToken) {

        String newAccessToken = authCommandUseCase.refreshAccessToken(RefreshTokenCommand.of(refreshToken));

        RefreshTokenResponse response = RefreshTokenResponse.of(newAccessToken);

        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다. Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse httpResponse) {

        authCommandUseCase.logout(userDetails.getUserId());

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());


        return ResponseEntity.ok(BaseResponse.success("로그아웃 성공", null));
    }
}
