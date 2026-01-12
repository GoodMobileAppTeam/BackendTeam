package mobile.backend.auth.domain.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSignupCommand {
    private final String email;
    private final String password;
    private final String name;

    public static TestSignupCommand of(String email, String password, String name) {
        return new TestSignupCommand(email, password, name);
    }
}
