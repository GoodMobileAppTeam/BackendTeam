package mobile.backend.videoEdit.adapter.in.web.response;

import mobile.backend.videoEdit.domain.model.VideoEditSummary;

import java.time.LocalDate;
import java.util.List;

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
