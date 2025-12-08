package mobile.backend.auth.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mobile.backend.auth.domain.model.AuthToken;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestAuthResponse {
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;

    public static TestAuthResponse from(AuthToken authToken) {
        return new TestAuthResponse(
                authToken.getUserId(),
                authToken.getAccessToken(),
                authToken.getRefreshToken()
        );
    }
}
