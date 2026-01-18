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
    FUTURE_DATE_NOT_ALLOWED("미래 날짜는 조회할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CURSOR("커서 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    PLACE_NAME_NOT_FOUND("좌표에 해당하는 장소 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_KAKAO_RESPONSE("카카오 지도 API 응답이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    OVER_VIDEO_COUNT("하루에 저장할 수 있는 영상 개수(5개)를 초과했습니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}
