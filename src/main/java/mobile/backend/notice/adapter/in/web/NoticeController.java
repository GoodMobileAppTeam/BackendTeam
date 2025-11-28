package mobile.backend.notice.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.global.adapter.in.web.response.SimplePageResponse;
import mobile.backend.notice.adapter.in.web.response.NoticeResponse;
import mobile.backend.notice.application.port.in.NoticeCommandUseCase;
import mobile.backend.notice.application.port.in.NoticeQueryUseCase;
import mobile.backend.notice.domain.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지 API", description = "공지 전용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notices")
public class NoticeController {

  private final NoticeCommandUseCase noticeCommandUseCase;
  private final NoticeQueryUseCase noticeQueryUseCase;

  @Operation(summary = "공지 상세 조회", description = "공지 ID로 공지 상세 내용을 조회합니다.")
  @GetMapping("/{noticeId}")
  public BaseResponse<NoticeResponse> getNotice(@PathVariable Long noticeId) {
    Notice notice = noticeQueryUseCase.getNotice(noticeId);
    return BaseResponse.success(NoticeResponse.from(notice));
  }
  @Operation(summary = "공지 리스트 조회", description = "공지 목록을 최신순으로 페이징 조회합니다.")
  @GetMapping
  public BaseResponse<SimplePageResponse<NoticeResponse>> getNoticeList(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Page<NoticeResponse> result = noticeQueryUseCase.getNoticeList(PageRequest.of(page, size))
        .map(NoticeResponse::from);
    return BaseResponse.success(SimplePageResponse.from(result));
  }

  @Operation(summary = "홈 화면 최신 공지 1건 조회", description = "홈 화면 진입 시 보여줄 최신 공지 1건을 조회합니다. 사용자가 다시보지 않기를 누른 공지는 반환하지 않습니다.")
  @GetMapping("/home/{userId}")
  public BaseResponse<NoticeResponse> getHomeNotice(@PathVariable Long userId) { // TODO: JWT 쓰면 @AuthenticationPrincipal로 교체
    Notice notice = noticeQueryUseCase.getHomeNotice(userId);

    if (notice == null) {
      return BaseResponse.success("현재 볼 수 있는 최신 공지가 없습니다.", null);
    }

    return BaseResponse.success(NoticeResponse.from(notice));
  }

  @Operation(summary = "공지 다시보지 않기", description = "특정 공지에 대해 다시보지 않기(ignored)를 설정합니다. 토큰 필수")
  @PostMapping("/{noticeId}/{userId}/ignore")
  public BaseResponse<Void> ignoreNotice(
      @PathVariable Long userId,
      @PathVariable Long noticeId
  ) {
    noticeCommandUseCase.ignoreNotice(userId, noticeId);
    String message = String.format("공지 %d번을 다시보지 않기로 설정했습니다.", noticeId);
    return BaseResponse.success(message, null);
  }


}
