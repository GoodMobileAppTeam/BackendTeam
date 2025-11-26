package mobile.backend.videoEdit.adapter.in.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;


@Schema(description = "영상 등록 요청 DTO")
public record CreateVideoEditRequest(
        @Schema(description = "앨범 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "앨범 ID는 필수입니다.")
        Long albumId,

        @Schema(description = "영상 길이 (초 단위)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "영상 길이는 필수입니다.")
        @Positive(message = "영상 길이는 양수여야 합니다.")
        @Max(value = 60, message = "영상 길이는 60초를 초과할 수 없습니다.")
        Integer duration,

        @Schema(description = "S3에 업로드된 영상 URL", example = "https://bucket.s3.amazonaws.com/videos/sample.mp4", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "영상 URL은 필수입니다.")
        String videoUrl,

        @Schema(description = "사용자가 선택한 영상 기록 날짜 (날짜별 조회에 사용)", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "저장 시간은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate saveTime,

        @Schema(description = "영상 설명 (선택사항)", example = "오늘의 운동 영상")
        String description
) {}
