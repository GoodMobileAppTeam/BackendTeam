package mobile.backend.user.domain.command;

import lombok.Getter;
import mobile.backend.user.domain.model.SocialType;

@Getter
public class SocialLoginCommand {
  private final SocialType socialType;
  private final String accessToken;

  public SocialLoginCommand(SocialType socialType, String accessToken) {
    this.socialType = socialType;
    this.accessToken = accessToken;
  }

  public static SocialLoginCommand of(SocialType socialType, String accessToken) {
    return new SocialLoginCommand(socialType, accessToken);
  }
}
