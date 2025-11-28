package mobile.backend.auth.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthToken {
    private Long userId;
    private String accessToken;
    private String refreshToken;

    public static AuthToken of(Long userId, String accessToken, String refreshToken) {
        return AuthToken.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}