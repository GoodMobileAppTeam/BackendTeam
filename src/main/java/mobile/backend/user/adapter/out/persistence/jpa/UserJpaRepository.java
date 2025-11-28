package mobile.backend.user.adapter.out.persistence.jpa;

import mobile.backend.user.adapter.out.persistence.entity.UserEntity;
import mobile.backend.user.domain.model.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
