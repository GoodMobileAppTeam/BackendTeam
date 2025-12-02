package mobile.backend.auth.application.port.out;

import mobile.backend.auth.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    RefreshToken findByUserIdOrThrow(Long userId);
    void deleteByUserId(Long userId);
}