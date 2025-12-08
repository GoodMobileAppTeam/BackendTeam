package mobile.backend.user.application.port.out;

import mobile.backend.user.domain.model.TestUser;
import java.util.Optional;

public interface TestUserRepository {
    Optional<TestUser> findByEmail(String email);
    TestUser save(TestUser testUser);
}
