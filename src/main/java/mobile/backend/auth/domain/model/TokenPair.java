package mobile.backend.auth.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPair {
    private Long userId;
    private String accessToken;
    private String refreshToken;

    public static TokenPair of(Long userId, String accessToken, String refreshToken) {
        return TokenPair.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}