package mobile.backend.admin.domain.command;

import lombok.Getter;

@Getter
public class AdminLoginCommand {
  private final String username;
  private final String password;

  public AdminLoginCommand(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public static AdminLoginCommand of(String username, String password) {
    return new AdminLoginCommand(username, password);
  }
}
