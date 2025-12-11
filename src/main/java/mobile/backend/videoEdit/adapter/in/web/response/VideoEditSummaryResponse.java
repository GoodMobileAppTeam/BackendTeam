package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "영상 요약 정보 목록 응답 DTO")
public record VideoEditSummaryResponse(
        LocalDate saveTime,
        String latestThumbnail,
        Long count
) {
    public static VideoEditSummaryResponse fromDomain(VideoEditSummary summary) {
        return new VideoEditSummaryResponse(
                summary.saveTime(),
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
