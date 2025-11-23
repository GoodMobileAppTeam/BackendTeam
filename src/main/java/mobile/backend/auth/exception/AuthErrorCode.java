package mobile.backend.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    // 400 에러
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "40001", "파라미터 오류"),

    // 401 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "40002", "인증 실패"),
    INVALID_SOCIAL_TOKEN(HttpStatus.UNAUTHORIZED, "40002", "소셜 토큰 검증 실패"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "40002", "Refresh token invalid or expired"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "40002", "토큰 검증 실패"),

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50001", "Internal Server Error");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
