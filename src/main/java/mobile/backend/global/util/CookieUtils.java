package mobile.backend.global.util;

import mobile.backend.global.security.jwt.JwtProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final JwtProperties jwtProperties;

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(getAccessTokenCookieMaxAge())
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(getRefreshTokenCookieMaxAge())
                .sameSite("Strict")
                .build();
    }

    private int getAccessTokenCookieMaxAge() {
        return (int) (jwtProperties.getAccessTokenExpireTime() / 1000);
    }

    private int getRefreshTokenCookieMaxAge() {
        return (int) (jwtProperties.getRefreshTokenExpireTime() / 1000);
    }
}
