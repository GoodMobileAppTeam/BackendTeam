package mobile.backend.videoEdit.adapter.in.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;


public record CreateVideoEditRequest(
        @NotNull(message = "앨범 ID는 필수입니다.")
        Long albumId,

        @NotNull(message = "영상 길이는 필수입니다.")
        @Positive(message = "영상 길이는 양수여야 합니다.")
        @Max(value = 60, message = "영상 길이는 60초를 초과할 수 없습니다.")
        Integer duration,

        @NotBlank(message = "영상 URL은 필수입니다.")
        String videoUrl,

        @NotNull(message = "저장 시간은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate saveTime
) {}
