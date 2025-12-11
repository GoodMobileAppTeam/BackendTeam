package mobile.backend.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.user.adapter.out.persistence.entity.TestUserEntity;
import mobile.backend.user.adapter.out.persistence.jpa.TestUserJpaRepository;
import mobile.backend.user.application.port.out.TestUserRepository;
import mobile.backend.user.domain.model.TestUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import mobile.backend.global.exception.CustomException;
import mobile.backend.user.exception.UserErrorCode;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TestUserRepositoryImpl implements TestUserRepository {

    private final TestUserJpaRepository testUserJpaRepository;

    @Override
    public Optional<TestUser> findByEmail(String email) {
        return testUserJpaRepository.findByEmail(email)
                .map(TestUserEntity::toDomain);
    }

    @Override
    public TestUser save(TestUser testUser) {
        try {
            TestUserEntity entity = TestUserEntity.from(testUser);
            TestUserEntity savedEntity = testUserJpaRepository.save(entity);
            return savedEntity.toDomain();
        } catch (DataIntegrityViolationException e) {
            // 에러 메시지에 "email"이 포함되어 있으면 이메일 중복
            if (e.getMessage() != null && e.getMessage().contains("email")) {
                throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
            }
            // 그 외의 경우는 일반적인 저장 실패
            throw new CustomException(UserErrorCode.USER_SAVE_FAILED);
        }
    }
}
