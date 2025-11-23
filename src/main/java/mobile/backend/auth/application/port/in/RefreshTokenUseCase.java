package mobile.backend.auth.application.port.in;

import mobile.backend.auth.domain.command.RefreshTokenCommand;

public interface RefreshTokenUseCase {
    String execute(RefreshTokenCommand command);
}
