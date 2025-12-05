package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {

    @Query("""
        SELECT v FROM VideoEditEntity v
        WHERE v.userId = :userId
        AND (:year IS NULL OR YEAR(v.saveTime) = :year)
        AND (:month IS NULL OR MONTH(v.saveTime) = :month)
        AND (:isBookMarked IS NULL OR v.isBookMark = :isBookMarked)
        ORDER BY v.saveTime DESC
        """)
    Page<VideoEditEntity> search(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("isBookMarked") Boolean isBookMarked,
            Pageable pageable
    );
}
