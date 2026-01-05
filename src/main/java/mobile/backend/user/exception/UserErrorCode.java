package mobile.backend.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mobile.backend.global.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
  UNAUTHORIZED_ACCESS("본인의 계정만 접근할 수 있습니다.", HttpStatus.FORBIDDEN),
  USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_SAVE_FAILED("유저 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final String message;
  private final HttpStatus status;
}
