package mobile.backend.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mobile.backend.global.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdminAuthErrorCode implements BaseErrorCode {
  INVALID_CREDENTIAL("아이디 또는 비밀번호가 잘못되었습니다.", HttpStatus.BAD_REQUEST),
  ;

  private final String message;
  private final HttpStatus status;
}
