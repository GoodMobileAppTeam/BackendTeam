package mobile.backend.admin.application.port.in;

import jakarta.servlet.http.HttpSession;
import mobile.backend.admin.domain.command.AdminLoginCommand;

public interface AdminLoginUseCase {
  void login(AdminLoginCommand command, HttpSession session);
}
