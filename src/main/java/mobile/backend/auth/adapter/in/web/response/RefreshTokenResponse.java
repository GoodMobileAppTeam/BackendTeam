package mobile.backend.auth.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {
    private String accessToken;

    public static RefreshTokenResponse of(String accessToken) {
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
