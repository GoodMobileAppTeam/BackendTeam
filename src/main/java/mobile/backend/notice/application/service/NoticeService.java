package mobile.backend.notice.application.service;

import lombok.RequiredArgsConstructor;
import mobile.backend.notice.application.port.in.NoticeCommandUseCase;
import mobile.backend.notice.application.port.in.NoticeQueryUseCase;
import mobile.backend.notice.application.port.out.NoticeRepository;
import mobile.backend.notice.domain.command.NoticeCommand;
import mobile.backend.notice.domain.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService implements NoticeCommandUseCase, NoticeQueryUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  @Transactional
  public Notice createNotice(NoticeCommand command) {
    Notice notice = Notice.create(command.getTitle(), command.getContent());
    return noticeRepository.save(notice);
  }

  @Override
  @Transactional
  public void ignoreNotice(Long userId, Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId);
    noticeRepository.ignore(userId, notice.getId());
  }

  @Override
  public Notice getNotice(Long noticeId) {
    return noticeRepository.findById(noticeId);
  }

  @Override
  public Page<Notice> getNoticeList(Pageable pageable) {
    return noticeRepository.findNotices(pageable);
  }

  @Override
  public Notice getHomeNotice(Long userId) {
    Notice latest = noticeRepository.findLatest();
    boolean ignored = noticeRepository.isIgnored(userId, latest.getId());
    return ignored ? null : latest;
  }
}
