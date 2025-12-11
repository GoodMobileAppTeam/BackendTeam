package mobile.backend.auth.domain.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestLoginCommand {
    private final String email;
    private final String password;

    public static TestLoginCommand of(String email, String password) {
        return new TestLoginCommand(email, password);
    }
}
