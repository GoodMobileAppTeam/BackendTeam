package mobile.backend.auth.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginResponse {
    private Long userId;
    private String accessToken;

    public static SocialLoginResponse of(Long userId, String accessToken) {
        return new SocialLoginResponse(userId, accessToken);
    }
}
