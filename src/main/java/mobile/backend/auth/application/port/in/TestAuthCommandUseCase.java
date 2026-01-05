package mobile.backend.auth.application.port.in;

import mobile.backend.auth.domain.command.TestSignupCommand;
import mobile.backend.auth.domain.command.TestLoginCommand;
import mobile.backend.auth.domain.model.AuthToken;
import mobile.backend.user.domain.model.User;

public interface TestAuthCommandUseCase {
    User signup(TestSignupCommand command);
    AuthToken login(TestLoginCommand command);
}
