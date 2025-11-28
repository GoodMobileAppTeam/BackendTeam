package mobile.backend.auth.adapter.out.persistence.jpa;

import mobile.backend.auth.adapter.out.persistence.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisCrudRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByUserId(Long userId);
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserId(Long userId);
}