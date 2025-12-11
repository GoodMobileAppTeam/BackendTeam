package mobile.backend.videoEdit.adapter.out.persistence.jpa;

public record VideoDailySummaryProjection(
        java.sql.Date saveTime,
        String latestThumbnail,
        Long count
) {}
