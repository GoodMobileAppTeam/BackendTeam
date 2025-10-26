package mobile.backend.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

  String getMessage();

  HttpStatus getStatus();

  default int getStatusValue() {
    return getStatus().value();
  }

  default String getCode() {
    return ((Enum<?>) this).name();
  }

}
