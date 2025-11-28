package mobile.backend.auth.adapter.out.persistence.entity;

import mobile.backend.auth.domain.model.RefreshToken;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshTokenEntity {

    @Id
    private String id;  // Redis key

    @Indexed
    @NotNull
    private Long userId;

    @NotNull
    private String token;

    @NotNull
    private LocalDateTime expiresAt;

    @TimeToLive
    @NotNull
    private Long ttl;  // 초 단위

    @Builder
    public RefreshTokenEntity(Long userId, String token, LocalDateTime expiresAt, Long ttl) {
        this.id = "refresh:" + userId;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.ttl = ttl;
    }

    public static RefreshTokenEntity from(Long userId, String token, LocalDateTime expiresAt, Long ttlSeconds) {
        return RefreshTokenEntity.builder()
                .userId(userId)
                .token(token)
                .expiresAt(expiresAt)
                .ttl(ttlSeconds)
                .build();
    }

    public RefreshToken toDomain() {
        return RefreshToken.of(
                this.userId,
                this.token,
                this.expiresAt
        );
    }

}