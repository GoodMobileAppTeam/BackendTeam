package mobile.backend.auth.domain.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenCommand {
    private String refreshToken;

    public static RefreshTokenCommand of(String refreshToken) {
        return RefreshTokenCommand.builder()
                .refreshToken(refreshToken)
                .build();
    }
}