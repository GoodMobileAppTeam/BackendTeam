package mobile.backend.admin.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.admin.adapter.in.web.request.AdminLoginRequest;
import mobile.backend.admin.application.port.in.AdminLoginUseCase;
import mobile.backend.admin.domain.command.AdminLoginCommand;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "어드민 인증 API", description = "어드민 페이지 인증 API")
@Slf4j
@RequiredArgsConstructor
public class AdminAuthController {

  private final AdminLoginUseCase adminLoginUseCase;

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<Void>> login(
      @RequestBody AdminLoginRequest request,
      HttpSession session
  ) {
    AdminLoginCommand command = request.toCommand();
    adminLoginUseCase.login(command, session);
    return ResponseEntity.ok(BaseResponse.success("로그인 성공", null));
  }

  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok(BaseResponse.success("로그아웃 성공", null));
  }

  @GetMapping("/auth/check")
  public ResponseEntity<BaseResponse<Boolean>> checkLogin(HttpSession session) {
    boolean loggedIn = session.getAttribute("ADMIN_AUTH") != null;
    return ResponseEntity.ok(BaseResponse.success(loggedIn));
  }

}