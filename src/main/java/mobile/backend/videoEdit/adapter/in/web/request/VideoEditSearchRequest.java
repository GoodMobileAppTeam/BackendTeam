package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Schema(description = "영상 검색 요청 DTO")
public record VideoEditSearchRequest(
        @Schema(description = "조회할 년도 (선택사항)", example = "2024")
        @Min(value = 2000, message = "년도는 2000 이상이어야 합니다.")
        @Max(value = 2100, message = "년도는 2100 이하여야 합니다.")
        Integer year,

        @Schema(description = "조회할 월 (1~12, 선택사항)", example = "1")
        @Min(value = 1, message = "월은 1 이상이어야 합니다.")
        @Max(value = 12, message = "월은 12 이하여야 합니다.")
        Integer month,

        @Schema(description = "북마크 필터 (true: 북마크된 영상만, false: 북마크 안된 영상만, null: 전체)", example = "true")
        Boolean isBookMarked,

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
        Integer page,

        @Schema(description = "페이지 크기 (최대 100)", example = "20", defaultValue = "20")
        @Positive(message = "사이즈는 양수여야 합니다.")
        @Max(value = 100, message = "사이즈는 100 이하여야 합니다.")
        Integer size
) {
    public VideoEditSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
    }
}
