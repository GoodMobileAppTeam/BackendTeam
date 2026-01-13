package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {

    /**
     * 특정 사용자의 기간 내 영상 편집 데이터를 날짜 단위로 요약 조회한다.
     *
     * [조회 목적]
     * - 하루(user_defined_date) 기준으로
     *   1) 해당 날짜의 총 영상 편집 개수
     *   2) 가장 최근에 생성된 영상의 썸네일
     * 를 함께 조회하기 위함
     *
     * [조회 결과]
     * - userDefinedDate : 사용자가 지정한 날짜
     * - count           : 해당 날짜의 영상 편집 총 개수
     * - latestThumbnail : 해당 날짜에 생성된 영상 중 가장 최근 영상의 썸네일
     *
     * [쿼리 동작 방식]
     * 1) 서브쿼리(t)
     *    - user_id, 기간(startDate ~ endDate) 기준으로 필터링
     *    - user_defined_date별로 그룹화
     *    - 각 날짜별
     *        · COUNT(*)        → 영상 편집 개수
     *        · MAX(created_at) → 가장 최근 생성 시각
     *      을 계산
     *
     * 2) 메인 쿼리(video_edit v)
     *    - 서브쿼리 결과와
     *      (user_defined_date, created_at = latestCreatedAt) 기준으로 JOIN
     *    - 이를 통해 "각 날짜의 가장 최근 영상 1건"을 정확히 식별
     *
     * 3) 최종 결과를 user_defined_date 기준 내림차순으로 정렬
     *
     * [향후 고려 사항]
     * - 사용자 사용량 증가 시
     *   · 테이블 분리
     *   · 일 단위 집계 테이블 도입
     *   등의 개선 방안을 검토 가능
     *
     *  - 사용자 사용량이 성능에 영향 없는 경우
     *   · 유지보수를 위해 QueryDsl로 수정
     */
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
