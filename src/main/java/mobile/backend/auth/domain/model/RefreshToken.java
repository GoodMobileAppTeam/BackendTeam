package mobile.backend.auth.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshToken {
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;

    public static RefreshToken of(Long userId, String token, LocalDateTime expiresAt) {
        return RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiresAt(expiresAt)
                .build();
    }

    public static RefreshToken create(Long userId, String token, Long expireTimeMillis) {
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(expireTimeMillis / 1000);
        return RefreshToken.of(userId, token, expiresAt);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}