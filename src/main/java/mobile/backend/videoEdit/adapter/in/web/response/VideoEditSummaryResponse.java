package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;

import java.time.LocalDate;
import java.util.List;

@Schema(
        title = "VideoEditSummaryResponse : 영상 요약 정보 목록 응답 DTO",
        description = """
        사용자 지정 날짜 기준 영상 요약 정보 응답 DTO입니다.

        - 각 요소는 특정 날짜에 대한 영상 요약 정보를 의미합니다.
        - 조회 결과가 없는 경우, 이 DTO를 요소로 갖는 목록은 빈 배열([])로 반환됩니다.

        예시 (데이터가 없는 경우):
        {
          "success": true,
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": []
        }
        """
)public record VideoEditSummaryResponse(

        @Schema(
                description = "사용자 지정 날짜",
                example = "2024-01-15"
        )
        LocalDate userDefinedDate,

        @Schema(
                description = "가장 최근 썸네일 URL",
                example = "https://bucket.s3.amazonaws.com/thumbnails/uuid.jpg"
        )
        String latestThumbnail,

        @Schema(
                description = "해당 날짜의 영상 개수",
                example = "3"
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
