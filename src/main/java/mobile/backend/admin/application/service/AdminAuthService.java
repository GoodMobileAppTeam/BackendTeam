package mobile.backend.admin.application.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mobile.backend.admin.application.port.in.AdminLoginUseCase;
import mobile.backend.admin.domain.command.AdminLoginCommand;
import mobile.backend.admin.exception.AdminAuthErrorCode;
import mobile.backend.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService implements AdminLoginUseCase {

  @Value("${ADMIN_USERNAME}")
  private String adminUsername;

  @Value("${ADMIN_PASSWORD}")
  private String adminPassword;

  @Override
  public void login(AdminLoginCommand command, HttpSession session) {
    boolean matchedAdmin = adminUsername.equals(command.getUsername())
        && adminPassword.equals(command.getPassword());

    if (!matchedAdmin) {
      throw new CustomException(AdminAuthErrorCode.INVALID_CREDENTIAL);
    }

    session.setAttribute("ADMIN_AUTH", true);
  }
}
