package mobile.backend.notice.adapter.in.web.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

  @Value("${ADMIN_USERNAME}")
  private String adminUsername;

  @Value("${ADMIN_PASSWORD}")
  private String adminPassword;

  @GetMapping("/login")
  public String loginForm() {
    return "admin/login";
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      HttpSession session,
      Model model
  ) {
    if (username.equals(adminUsername) && password.equals(adminPassword)) {
      session.setAttribute("ADMIN_AUTH", true);
      return "redirect:/admin/notices/new";
    }

    model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
    return "admin/login";
  }
}