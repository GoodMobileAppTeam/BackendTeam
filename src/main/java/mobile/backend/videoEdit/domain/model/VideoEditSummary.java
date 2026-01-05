package mobile.backend.videoEdit.domain.model;

import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoDailySummaryProjection;

import java.time.LocalDate;

public record VideoEditSummary(
        LocalDate userDefinedDate,
        String latestThumbnail,
        Long count
) {
    public static VideoEditSummary fromProjection(VideoDailySummaryProjection videoDailySummaryProjection) {
        return new VideoEditSummary(
                videoDailySummaryProjection.userDefinedDate().toLocalDate(),
                videoDailySummaryProjection.latestThumbnail(),
                videoDailySummaryProjection.count()
        );
    }
}
