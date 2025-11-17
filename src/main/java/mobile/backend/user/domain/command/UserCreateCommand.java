package mobile.backend.user.domain.command;

import lombok.Getter;
import mobile.backend.user.domain.model.SocialType;

@Getter
public class UserCreateCommand {
  private final String name;
  private final String socialId;
  private final SocialType socialType;

  public UserCreateCommand(String name, String socialId, SocialType socialType) {
    this.name = name;
    this.socialId = socialId;
    this.socialType = socialType;
  }

  public static UserCreateCommand of(String name, String socialId, SocialType socialType) {
    return new UserCreateCommand(name, socialId, socialType);
  }
}
