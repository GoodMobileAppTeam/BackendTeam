package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {

    @Query("""
    SELECT v
    FROM VideoEditEntity v
    WHERE v.userId = :userId
      AND v.saveTime <= :baseDateEnd
    ORDER BY
      v.saveTime DESC,
      v.createdAt DESC,
      v.id DESC
""")
    List<VideoEditEntity> findInitPage(
            Long userId,
            LocalDate baseDateEnd,
            Pageable pageable
    );

    @Query("""
    SELECT v
    FROM VideoEditEntity v
    WHERE v.userId = :userId
      AND (
        v.saveTime < :cursorSaveTime
        OR (
          v.saveTime = :cursorSaveTime
          AND (
            v.createdAt < :cursorCreatedAt
            OR (
              v.createdAt = :cursorCreatedAt
              AND v.id < :cursorId
            )
          )
        )
      )
    ORDER BY
      v.saveTime DESC,
      v.createdAt DESC,
      v.id DESC
""")
    List<VideoEditEntity> findNextPage(
            @Param("userId") Long userId,
            @Param("cursorSaveTime") LocalDate cursorSaveTime,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
    SELECT v
    FROM VideoEditEntity v
    WHERE v.userId = :userId
      AND (
        v.saveTime > :cursorSaveTime
        OR (
          v.saveTime = :cursorSaveTime
          AND (
            v.createdAt > :cursorCreatedAt
            OR (
              v.createdAt = :cursorCreatedAt
              AND v.id > :cursorId
            )
          )
        )
      )
    ORDER BY
      v.saveTime ASC,
      v.createdAt ASC,
      v.id ASC
""")
    List<VideoEditEntity> findPrevPage(
            @Param("userId") Long userId,
            @Param("cursorSaveTime") LocalDate cursorSaveTime,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
        SELECT v FROM VideoEditEntity v
        WHERE v.userId = :userId
            AND v.isBookMark = true
            AND (
                :cursorDate IS NULL
                OR v.saveTime < :cursorDate
                OR (v.saveTime = :cursorDate AND v.id < :cursorId)
            )
        ORDER BY v.saveTime DESC, v.id DESC
    """)
    List<VideoEditEntity> findBookmarkedByCursor(
            @Param("userId") Long userId,
            @Param("cursorDate") LocalDate cursorDate,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

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
}
