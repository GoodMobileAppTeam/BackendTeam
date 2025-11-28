package mobile.backend.notice.application.port.out;

import mobile.backend.notice.domain.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepository {
  Notice save(Notice notice);
  Notice findById(Long noticeId);
  Page<Notice> findNotices(Pageable pageable);
  Notice findLatest();
  boolean isIgnored(Long userId, Long noticeId);
  void ignore(Long userId, Long noticeId);
}
