package mobile.backend.auth.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.auth.adapter.out.persistence.entity.RefreshTokenEntity;
import mobile.backend.auth.adapter.out.persistence.jpa.RefreshTokenRedisCrudRepository;
import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import mobile.backend.auth.domain.model.RefreshToken;
import mobile.backend.global.security.jwt.JwtProperties;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenRedisCrudRepository redisRepository;
    private final JwtProperties jwtProperties;

    @Override
    public void save(RefreshToken refreshToken) {
        try {
            Long ttlSeconds = jwtProperties.getRefreshTokenExpireTime() / 1000;

            RefreshTokenEntity entity = RefreshTokenEntity.from(
                    refreshToken.getUserId(),
                    refreshToken.getToken(),
                    refreshToken.getExpiresAt(),
                    ttlSeconds
            );

            redisRepository.save(entity);
        } catch (Exception e) {
            log.error("Refresh Token 저장 실패: userId={}", refreshToken.getUserId(), e);
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_SAVE_FAILED);
        }
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        try {
            return redisRepository.findByUserId(userId)
                    .map(RefreshTokenEntity::toDomain);
        } catch (Exception e) {
            log.error("Refresh Token 조회 실패: userId={}", userId, e);
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_FETCH_FAILED);
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        try {
            return redisRepository.findByToken(token)
                    .map(RefreshTokenEntity::toDomain);
        } catch (Exception e) {
            log.error("Refresh Token 조회 실패: token={}", token, e);
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_FETCH_FAILED);
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        try {
            //RefreshTokenEntity에 "refresh:" + userId 형식으로 생성되기에 ID를 직접 만들어서 deleteById 사용
            String id = "refresh:" + userId;
            redisRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Refresh Token 삭제 실패: userId={}", userId, e);
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_DELETE_FAILED);
        }
    }

}
