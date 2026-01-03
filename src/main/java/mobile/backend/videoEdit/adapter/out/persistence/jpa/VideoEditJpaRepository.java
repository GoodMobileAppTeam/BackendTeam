package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {
    @Query(value = """
    SELECT 
        v.save_time AS saveTime,
        v.thumbnail_url AS latestThumbnail,
        t.cnt AS count
    FROM video_edit v
    INNER JOIN (
        SELECT 
            save_time,
            COUNT(*) AS cnt,
            MAX(created_at) AS latestCreatedAt
        FROM video_edit
        WHERE user_id = :userId
          AND save_time BETWEEN :startDate AND :endDate
        GROUP BY save_time
    ) t
        ON v.save_time = t.save_time
       AND v.created_at = t.latestCreatedAt
    WHERE v.user_id = :userId
      AND v.save_time BETWEEN :startDate AND :endDate
    ORDER BY v.save_time DESC
    """,
            nativeQuery = true
    )
    List<VideoDailySummaryProjection> findDailySummary(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<VideoEditEntity> findAllByUserIdAndSaveTimeOrderByCreatedAtDesc(Long userId, LocalDate saveTime);
}
