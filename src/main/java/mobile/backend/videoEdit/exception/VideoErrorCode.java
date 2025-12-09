package mobile.backend.videoEdit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mobile.backend.global.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VideoErrorCode implements BaseErrorCode {

    VIDEO_NOT_FOUND("영상 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VIDEO_ACCESS_DENIED("해당 영상에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FILE_STORAGE_ERROR("파일 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_DATE_RANGE("startDate는 endDate보다 이후일 수 없습니다.", HttpStatus.BAD_REQUEST),
    FUTURE_DATE_NOT_ALLOWED("미래 날짜는 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
