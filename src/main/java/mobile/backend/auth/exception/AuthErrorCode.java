package mobile.backend.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    // 400 에러
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 오류"),

    // 401 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 실패"),
    INVALID_SOCIAL_TOKEN(HttpStatus.UNAUTHORIZED, "소셜 토큰 검증 실패"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token invalid"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token expired"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 검증 실패"),
    // 테스트 로그인용
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 409 에러 (테스트 로그인용)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    REFRESH_TOKEN_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh Token 저장 실패"),
    REFRESH_TOKEN_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh Token 조회 실패"),
    REFRESH_TOKEN_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh Token 삭제 실패");



    private final HttpStatus status;
    private final String message;
}
