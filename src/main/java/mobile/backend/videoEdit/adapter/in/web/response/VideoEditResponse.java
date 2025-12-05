package mobile.backend.videoEdit.adapter.in.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;

@Schema(description = "영상 응답 DTO")
public record VideoEditResponse(

        @Schema(description = "영상 ID", example = "1")
        Long id,

        @Schema(description = "앨범 ID", example = "1")
        Long albumId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "영상 길이 (초)", example = "45")
        Integer duration,

        @Schema(description = "영상 URL", example = "https://bucket.s3.amazonaws.com/videos/sample.mp4")
        String videoUrl,

        @Schema(description = "사용자가 선택한 기록 날짜", example = "2024-01-15")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate saveTime,

        @Schema(description = "북마크 여부", example = "false")
        boolean isBookMarked,

        @Schema(description = "썸네일 URL", example = "https://bucket.s3.amazonaws.com/thumbnails/uuid.jpg")
        String thumbnailUrl,

        @Schema(description = "영상 설명", example = "오늘의 운동 영상")
        String description

) {
    public static VideoEditResponse from(VideoEdit domain) {
        return new VideoEditResponse(
                domain.getId(),
                domain.getAlbumId(),
                domain.getUserId(),
                domain.getDuration(),
                domain.getVideoUrl(),
                domain.getSaveTime(),
                domain.isBookMarked(),
                domain.getThumbnailUrl(),
                domain.getDescription()
        );
    }
}
