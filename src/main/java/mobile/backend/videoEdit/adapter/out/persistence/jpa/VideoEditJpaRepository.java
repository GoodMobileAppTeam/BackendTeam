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

    /**
     * 특정 사용자가 지정한 날짜(userDefinedDate)에 해당하는 영상 데이터들을 조회한다.
     *
     * - 조회 조건
     *   1) userId가 일치하는 데이터
     *   2) userDefinedDate가 전달된 날짜와 동일한 데이터
     *
     * - 정렬 방식
     *   createdAt 기준 내림차순 (가장 최근에 생성된 영상이 먼저 조회됨)
     *
     * - 반환 값
     *   조건에 맞는 VideoEditEntity 목록
     *   (조건에 맞는 데이터가 없을 경우 빈 리스트 반환)
     */
    List<VideoEditEntity> findAllByUserIdAndUserDefinedDateOrderByCreatedAtDesc(Long userId, LocalDate userDefinedDate);
}
