package mobile.backend.auth.application.port.in;

import mobile.backend.auth.domain.command.RefreshTokenCommand;
import mobile.backend.auth.domain.command.SocialLoginCommand;
import mobile.backend.auth.domain.model.AuthToken;

public interface AuthCommandUseCase {
    AuthToken login(SocialLoginCommand command);

    String refreshAccessToken(RefreshTokenCommand command);

    void logout(Long userId);
}
