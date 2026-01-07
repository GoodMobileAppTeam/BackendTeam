package mobile.backend.videoEdit.adapter.out.persistence.jpa;

public record VideoDailySummaryProjection(
        java.sql.Date userDefinedDate,
        String latestThumbnail,
        Long count
) {}
