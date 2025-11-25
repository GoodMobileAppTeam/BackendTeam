package mobile.backend.notice.adapter.in.web.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mobile.backend.notice.adapter.in.web.request.NoticeRequest;
import mobile.backend.notice.application.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

  private final NoticeService noticeService;

  @GetMapping("/new")
  public String noticeForm(HttpSession session, Model model) {
    if (session.getAttribute("ADMIN_AUTH") == null) {
      return "redirect:/admin/login";
    }
    model.addAttribute("noticeRequest", new NoticeRequest());
    return "admin/notice-form";
  }

  @PostMapping
  public String createNotice(
      @ModelAttribute NoticeRequest noticeRequest,
      HttpSession session,
      Model model
  ) {
    if (session.getAttribute("ADMIN_AUTH") == null) {
      return "redirect:/admin/login";
    }

    noticeService.createNotice(noticeRequest.toCommand());
    model.addAttribute("successMessage", "공지 작성이 완료되었습니다.");
    return "admin/notice-form";
  }
}