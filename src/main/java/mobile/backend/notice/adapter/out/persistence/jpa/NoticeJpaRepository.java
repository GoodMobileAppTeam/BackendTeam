package mobile.backend.notice.adapter.out.persistence.jpa;

import java.util.Optional;
import mobile.backend.notice.adapter.out.persistence.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<NoticeEntity, Long> {
  Page<NoticeEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
  Optional<NoticeEntity> findFirstByOrderByCreatedAtDesc();
}
