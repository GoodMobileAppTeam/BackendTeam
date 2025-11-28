package mobile.backend.admin.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import mobile.backend.admin.domain.command.AdminLoginCommand;

@Getter
@Schema(title = "AdminLoginRequest : 어드민페이지 로그인 요청 DTO")
public class AdminLoginRequest {

  @NotBlank(message = "아이디는 필수입니다.")
  private String username;

  @NotBlank(message = "비밀번호는 필수입니다.")
  private String password;

  public AdminLoginCommand toCommand() {
    return AdminLoginCommand.of(this.username, this.password);
  }
}
