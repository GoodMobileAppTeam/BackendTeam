package mobile.backend.videoEdit.adapter.out.persistence.jpa;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditJpaRepository extends JpaRepository<VideoEditEntity, Long> {

    @Query("""
        SELECT v FROM VideoEditEntity v
        WHERE v.userId = :userId
        AND v.saveTime BETWEEN :startDate AND :endDate
        ORDER BY v.saveTime DESC
    """)
    List<VideoEditEntity> findByDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT v FROM VideoEditEntity v
        WHERE v.userId = :userId
        AND v.isBookMark = true
        ORDER BY v.saveTime DESC
    """)
    List<VideoEditEntity> findBookmarked(
            @Param("userId") Long userId
    );
}
