package mobile.backend.auth.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResponse {
    private Long userId;
    private String refreshToken;

    public static SocialLoginResponse of(Long userId, String refreshToken) {
        return SocialLoginResponse.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
    }
}
