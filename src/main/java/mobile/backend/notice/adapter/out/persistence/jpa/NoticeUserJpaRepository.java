package mobile.backend.notice.adapter.out.persistence.jpa;

import java.util.Optional;
import mobile.backend.notice.adapter.out.persistence.entity.NoticeUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeUserJpaRepository extends JpaRepository<NoticeUserEntity, Long> {
  Optional<NoticeUserEntity> findByUserIdAndNoticeId(Long userId, Long noticeId);
}
