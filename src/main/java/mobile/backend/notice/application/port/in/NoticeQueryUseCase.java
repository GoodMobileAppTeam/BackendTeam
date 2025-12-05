package mobile.backend.notice.application.port.in;

import mobile.backend.notice.domain.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeQueryUseCase {
  Notice getNotice(Long noticeId);
  Page<Notice> getNoticeList(Pageable pageable);

  /**
   * 홈 화면 진입시 보여줄 최신 공지 1건 조회
   * - 최신 공지 1건 가져오고
   * - 해당 유저가 ignored(다시보지않기) 했으면 null 반환
   * @param userId
   * @return
   */
  Notice getHomeNotice(Long userId);
}
