package mobile.backend.auth.application.port.in;

import mobile.backend.auth.domain.command.TestSignupCommand;
import mobile.backend.auth.domain.command.TestLoginCommand;
import mobile.backend.auth.domain.model.AuthToken;

public interface TestAuthCommandUseCase {
    AuthToken signup(TestSignupCommand command);
    AuthToken login(TestLoginCommand command);
}
