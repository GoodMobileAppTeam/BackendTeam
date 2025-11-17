package mobile.backend.user.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mobile.backend.user.domain.model.User;

@Getter
@Builder
@AllArgsConstructor
public class SocialLoginResponse {
  private Long userId;
  private String accessToken;
  private String refreshToken;

  public static SocialLoginResponse of(User user, String accessToken, String refreshToken) {
    return SocialLoginResponse.builder()
        .userId(user.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
