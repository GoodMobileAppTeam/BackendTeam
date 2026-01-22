package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;

import java.time.LocalDate;
import java.util.List;

@Schema(title = "VideoEditSummaryResponse : 영상 요약 정보 목록 응답 DTO")
public record VideoEditSummaryResponse(

        @Schema(
                description = "사용자 지정 날짜",
                example = "2024-01-15",
                nullable = false
        )
        LocalDate userDefinedDate,

        @Schema(
                description = "가장 최근 썸네일 URL",
                example = "https://bucket.s3.amazonaws.com/thumbnails/uuid.jpg",
                nullable = false
        )
        String latestThumbnail,

        @Schema(
                description = "해당 날짜의 영상 개수",
                example = "3",
                nullable = false
        )
        Long count
) {
    public static VideoEditSummaryResponse fromDomain(VideoEditSummary summary) {
        return new VideoEditSummaryResponse(
                summary.userDefinedDate(),
                summary.latestThumbnail(),
                summary.count()
        );
    }

    public static List<VideoEditSummaryResponse> fromDomainList(List<VideoEditSummary> summaries) {
        return summaries.stream()
                .map(VideoEditSummaryResponse::fromDomain)
                .toList();
    }
}
