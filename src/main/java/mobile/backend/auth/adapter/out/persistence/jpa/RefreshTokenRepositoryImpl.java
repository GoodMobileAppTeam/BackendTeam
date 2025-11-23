package mobile.backend.auth.adapter.out.persistence.jpa;

import lombok.RequiredArgsConstructor;
import mobile.backend.auth.adapter.out.persistence.entity.RefreshTokenEntity;
import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import mobile.backend.auth.domain.model.RefreshToken;
import mobile.backend.global.security.jwt.JwtProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenRedisRepository redisRepository;
    private final JwtProperties jwtProperties;

    @Override
    public void save(RefreshToken refreshToken) {
        Long ttlSeconds = jwtProperties.getRefreshTokenExpireTime() / 1000;

        RefreshTokenEntity entity = RefreshTokenEntity.from(
                refreshToken.getUserId(),
                refreshToken.getToken(),
                refreshToken.getExpiresAt(),
                ttlSeconds
        );

        redisRepository.save(entity);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return redisRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return redisRepository.findByToken(token)
                .map(this::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        //RefreshTokenEntity에 "refresh:" + userId 형식으로 생성되기에 ID를 직접 만들어서 deleteById 사용
        String id = "refresh:" + userId;
        redisRepository.deleteById(id);
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.of(
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt()
        );
    }
}
