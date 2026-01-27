package mobile.backend.videoEdit.adapter.in.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
        title = "VideoEditResponse : 영상 응답 DTO",
        description = """
        단일 영상 정보를 표현하는 응답 DTO입니다.

        - 이 DTO는 목록 형태(List)로 응답될 수 있습니다.
        - 조회 결과가 없는 경우, 이 DTO를 요소로 갖는 목록은
          빈 배열([])로 반환됩니다.

        예시 (데이터가 없는 경우):
        {
          "success": true,
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": []
        }
        """
)public record VideoEditResponse(

        @Schema(description = "영상 ID", example = "1")
        Long id,

        @Schema(description = "앨범 ID", example = "1")
        Long albumId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "영상 제목", example = "오늘의 영상")
        String title,

        @Schema(description = "영상 길이 (초)", example = "45")
        Integer duration,

        @Schema(
                description = "영상 URL",
                example = "https://bucket.s3.amazonaws.com/videos/sample.mp4"
        )
        String videoUrl,

        @Schema(
                description = "사용자가 선택한 기록 날짜 (선택값)",
                example = "2024-01-15"
        )
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate userDefinedDate,

        @Schema(
                description = "실제 DB에 저장된 생성 시각 (null able)",
                example = "2025-04-01T00:10:00"
        )
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss"
        )
        LocalDateTime createdAt,

        @Schema(description = "북마크 여부", example = "false")
        boolean isBookMarked,

        @Schema(
                description = "썸네일 URL",
                example = "https://bucket.s3.amazonaws.com/thumbnails/uuid.jpg"
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
