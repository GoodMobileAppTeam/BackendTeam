package mobile.backend.notice.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.notice.adapter.in.web.request.NoticeRequest;
import mobile.backend.notice.adapter.in.web.response.NoticeResponse;
import mobile.backend.notice.application.service.NoticeService;
import mobile.backend.notice.domain.model.Notice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/notices")
@Tag(name = "어드민 공지 API", description = "어드민 페이지 공지 API")
@RequiredArgsConstructor
public class AdminNoticeController {

  private final NoticeService noticeService;

  @PostMapping
  public ResponseEntity<BaseResponse<NoticeResponse>> createNotice(
      @RequestBody NoticeRequest noticeRequest,
      HttpSession session
  ) {
    if (session.getAttribute("ADMIN_AUTH") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(BaseResponse.error(401, "관리자 로그인이 필요합니다."));
    }

    Notice notice = noticeService.createNotice(noticeRequest.toCommand());
    return ResponseEntity.ok(BaseResponse.success(NoticeResponse.from(notice)));
  }
}