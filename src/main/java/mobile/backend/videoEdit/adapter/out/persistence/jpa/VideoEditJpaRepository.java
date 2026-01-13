package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {

    // 이후 유저 사용량에 따라 수정 여부 결정(유지 : queryDsl, 개선 : 테이블 분리 등 추가 방안 모색)
    @Query(value = """
        SELECT 
            v.user_defined_date AS userDefinedDate,
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
        """, nativeQuery = true)
    List<VideoDailySummaryProjection> findDailySummary(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 일일 영상 목록 반환 메서드 (CreatedAt을 기준으로 정렬)
    List<VideoEditEntity> findAllByUserIdAndUserDefinedDateOrderByCreatedAtDesc(Long userId, LocalDate userDefinedDate);
}
