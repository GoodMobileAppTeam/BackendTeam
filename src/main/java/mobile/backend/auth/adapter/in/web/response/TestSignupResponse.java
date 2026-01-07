package mobile.backend.auth.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mobile.backend.user.domain.model.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSignupResponse {
    private final Long userId;
    private final String email;
    private final String name;

    public static TestSignupResponse from(User user) {
        return new TestSignupResponse(
                user.getId(),
                user.getSocialId(),  // TEST 타입은 email이 socialId에 저장됨
                user.getName()
        );
    }
}
