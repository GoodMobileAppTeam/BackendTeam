package mobile.backend.user.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import mobile.backend.user.domain.model.SocialType;

@Getter
@Schema(title = "SocialLoginRequest : 소셜 로그인시 백엔드로 주는 요청 DTO")
public class SocialLoginRequest {
  private SocialType socialType;
  private String accessToken;

}
