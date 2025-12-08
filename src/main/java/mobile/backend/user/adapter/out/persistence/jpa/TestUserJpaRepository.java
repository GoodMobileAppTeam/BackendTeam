package mobile.backend.user.adapter.out.persistence.jpa;

import mobile.backend.user.adapter.out.persistence.entity.TestUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestUserJpaRepository extends JpaRepository<TestUserEntity, Long> {
    Optional<TestUserEntity> findByEmail(String email);
}
