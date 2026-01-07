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
        v.userDefinedDate AS userDefinedDate,
        v.thumbnail_url AS latestThumbnail,
        t.cnt AS count
    FROM video_edit v
    INNER JOIN (
        SELECT 
            user_defined_date,
            COUNT(*) AS cnt,
            MAX(created_at) AS latestCreatedAt
        FROM video_edit
        WHERE user_id = :userId
          AND user_defined_date BETWEEN :startDate AND :endDate
        GROUP BY user_defined_date
    ) t
        ON v.user_defined_date = t.user_defined_date
       AND v.created_at = t.latestCreatedAt
    WHERE v.user_id = :userId
      AND v.user_defined_date BETWEEN :startDate AND :endDate
    ORDER BY v.user_defined_date DESC
    """,
            nativeQuery = true
    )
    List<VideoDailySummaryProjection> findDailySummary(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<VideoEditEntity> findAllByUserIdAndUserDefinedDateOrderByCreatedAtDesc(Long userId, LocalDate userDefinedDate);
}
