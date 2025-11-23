package mobile.backend.auth.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.auth.domain.command.RefreshTokenCommand;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenCommand toCommand() {
        return RefreshTokenCommand.of(refreshToken);
    }
}
