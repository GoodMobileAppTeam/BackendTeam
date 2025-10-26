package mobile.backend.global.exception;

import lombok.Getter;
import mobile.backend.global.exception.model.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException {

  private final BaseErrorCode errorCode;

  public CustomException(BaseErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
