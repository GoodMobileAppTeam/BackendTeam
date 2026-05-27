package mobile.backend.notice.application.port.in;

import mobile.backend.notice.domain.command.NoticeCommand;
import mobile.backend.notice.domain.model.Notice;

public interface NoticeCommandUseCase {
  Notice createNotice(NoticeCommand command);

  Notice updateNotice(Long noticeId, NoticeCommand command);

  void deleteNotice(Long noticeId);

  /**
   * 공지 다시 보지 않기
   * @param userId
   * @param noticeId
   */
  void ignoreNotice(Long userId, Long noticeId);
}
