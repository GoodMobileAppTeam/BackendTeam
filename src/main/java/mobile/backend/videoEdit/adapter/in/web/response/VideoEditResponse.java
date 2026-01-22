package mobile.backend.videoEdit.adapter.in.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(title = "VideoEditResponse : 영상 응답 DTO")
public record VideoEditResponse(

        @Schema(description = "영상 ID", example = "1", nullable = false)
        Long id,

        @Schema(description = "앨범 ID", example = "1", nullable = false)
        Long albumId,

        @Schema(description = "사용자 ID", example = "1", nullable = false)
        Long userId,

        @Schema(description = "영상 제목", example = "오늘의 영상", nullable = false)
        String title,

        @Schema(description = "영상 길이 (초)", example = "45", nullable = false)
        Integer duration,

        @Schema(
                description = "영상 URL",
                example = "https://bucket.s3.amazonaws.com/videos/sample.mp4",
                nullable = false
        )
        String videoUrl,

        @Schema(
                description = "사용자가 선택한 기록 날짜 (선택값)",
                example = "2024-01-15",
                nullable = false
        )
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate userDefinedDate,

        @Schema(
                description = "실제 DB에 저장된 생성 시각 (null able)",
                example = "2025-04-01T00:10:00",
                nullable = true
        )
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss"
        )
        LocalDateTime createdAt,

        @Schema(description = "북마크 여부", example = "false", nullable = false)
        boolean isBookMarked,

        @Schema(
                description = "썸네일 URL",
                example = "https://bucket.s3.amazonaws.com/thumbnails/uuid.jpg",
                nullable = false
        )
        String thumbnailUrl
) {
    public static VideoEditResponse from(VideoEdit domain) {
        return new VideoEditResponse(
                domain.getId(),
                domain.getAlbumId(),
                domain.getUserId(),
                domain.getTitle(),
                domain.getDuration(),
                domain.getVideoUrl(),
                domain.getUserDefinedDate(),
                domain.getCreatedAt(),
                domain.isBookMarked(),
                domain.getThumbnailUrl()
        );
    }

    public static List<VideoEditResponse> from(List<VideoEdit> domains) {
        return domains.stream()
                .map(VideoEditResponse::from)
                .toList();
    }
}
