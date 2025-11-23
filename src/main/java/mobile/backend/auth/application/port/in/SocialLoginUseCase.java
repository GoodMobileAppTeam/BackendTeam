package mobile.backend.auth.application.port.in;

import mobile.backend.auth.domain.command.SocialLoginCommand;
import mobile.backend.auth.domain.model.TokenPair;

public interface SocialLoginUseCase {
    TokenPair execute(SocialLoginCommand command);
}