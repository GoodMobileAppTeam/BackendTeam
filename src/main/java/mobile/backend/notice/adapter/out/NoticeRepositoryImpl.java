package mobile.backend.notice.adapter.out;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.notice.adapter.out.persistence.entity.NoticeEntity;
import mobile.backend.notice.adapter.out.persistence.entity.NoticeUserEntity;
import mobile.backend.notice.adapter.out.persistence.jpa.NoticeJpaRepository;
import mobile.backend.notice.adapter.out.persistence.jpa.NoticeUserJpaRepository;
import mobile.backend.notice.application.port.out.NoticeRepository;
import mobile.backend.notice.domain.model.Notice;
import mobile.backend.notice.exception.NoticeErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

  private final NoticeJpaRepository noticeJpaRepository;
  private final NoticeUserJpaRepository noticeUserJpaRepository;

  @Override
  public Notice save(Notice notice) {
    NoticeEntity entity = NoticeEntity.from(notice);
    NoticeEntity saved = noticeJpaRepository.save(entity);
    return saved.toDomain();
  }

  @Override
  public Notice findById(Long noticeId) {
    NoticeEntity entity = noticeJpaRepository.findById(noticeId)
        .orElseThrow(() -> new CustomException(NoticeErrorCode.NOTICE_NOT_FOUND));
    return entity.toDomain();
  }

  @Override
  public Page<Notice> findNotices(Pageable pageable) {
    return noticeJpaRepository.findAllByOrderByCreatedAtDesc(pageable)
        .map(NoticeEntity::toDomain);
  }

  @Override
  public Notice findLatest() {
    NoticeEntity entity = noticeJpaRepository.findFirstByOrderByCreatedAtDesc()
        .orElseThrow(() -> new CustomException(NoticeErrorCode.NOTICE_NOT_FOUND));
    return entity.toDomain();
  }

  @Override
  public boolean isIgnored(Long userId, Long noticeId) {
    return noticeUserJpaRepository.findByUserIdAndNoticeId(userId, noticeId)
        .map(NoticeUserEntity::isIgnored)
        .orElse(false);
  }

  @Override
  public void ignore(Long userId, Long noticeId) {
    NoticeUserEntity entity = noticeUserJpaRepository
        .findByUserIdAndNoticeId(userId, noticeId)
        .orElse(NoticeUserEntity.of(userId, noticeId, false));

    NoticeUserEntity updated = NoticeUserEntity.updateIgnored(entity);
    noticeUserJpaRepository.save(updated);
  }
}
