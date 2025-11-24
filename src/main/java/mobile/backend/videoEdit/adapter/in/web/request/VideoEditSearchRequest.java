package mobile.backend.videoEdit.adapter.in.web.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record VideoEditSearchRequest(
        @Min(value = 2000, message = "년도는 2000 이상이어야 합니다.")
        @Max(value = 2100, message = "년도는 2100 이하여야 합니다.")
        Integer year,

        @Min(value = 1, message = "월은 1 이상이어야 합니다.")
        @Max(value = 12, message = "월은 12 이하여야 합니다.")
        Integer month,

        Boolean isBookMarked,

        @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
        Integer page,

        @Positive(message = "사이즈는 양수여야 합니다.")
        @Max(value = 100, message = "사이즈는 100 이하여야 합니다.")
        Integer size
) {
    public VideoEditSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
    }
}
