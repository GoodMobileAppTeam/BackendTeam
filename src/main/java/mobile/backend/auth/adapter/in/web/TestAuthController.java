package mobile.backend.auth.adapter.in.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mobile.backend.auth.adapter.in.web.request.TestSignupRequest;
import mobile.backend.auth.adapter.in.web.request.TestLoginRequest;
import mobile.backend.auth.adapter.in.web.response.TestAuthResponse;
import mobile.backend.auth.application.port.in.TestAuthCommandUseCase;
import mobile.backend.auth.domain.model.AuthToken;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mobile.backend.global.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


@Tag(name = "Swagger 테스트용 auth API", description = "Swagger에서 기능 테스트를 위한 테스트용 회원가입, 로그인 API")
@RestController
@RequestMapping("/v1/test-auth")
@RequiredArgsConstructor
public class TestAuthController {

    private final TestAuthCommandUseCase testAuthCommandUseCase;

    @PostMapping("/test-signup")
    public ResponseEntity<BaseResponse<TestAuthResponse>> signup(
            @Valid @RequestBody TestSignupRequest request,
            HttpServletResponse httpResponse) {

        AuthToken authToken = testAuthCommandUseCase.signup(request.toCommand());

        CustomUserDetails userDetails = new CustomUserDetails(authToken.getUserId());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", authToken.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("Strict")
                .build();


        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(BaseResponse.success(TestAuthResponse.from(authToken)));
    }


    @PostMapping("/test-login")
    public ResponseEntity<BaseResponse<TestAuthResponse>> login(
            @Valid @RequestBody TestLoginRequest request,
            HttpServletResponse httpResponse) {

        AuthToken authToken = testAuthCommandUseCase.login(request.toCommand());

        CustomUserDetails userDetails = new CustomUserDetails(authToken.getUserId());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", authToken.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(BaseResponse.success(TestAuthResponse.from(authToken)));
    }
}
