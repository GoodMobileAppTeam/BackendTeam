package mobile.backend.user.domain.model;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUser {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final LocalDateTime createdAt;

    // 테스트 사용자 생성 (회원가입용 - id 없음)
    public static TestUser create(String email, String encodedPassword, String name) {
        return new TestUser(null, name, email, encodedPassword, LocalDateTime.now());
    }

    // 테스트 사용자 생성 (DB 조회용 - id 있음)
    public static TestUser of(Long id, String name, String email, String password, LocalDateTime createdAt) {
        return new TestUser(id, name, email, password, createdAt);
    }
}
