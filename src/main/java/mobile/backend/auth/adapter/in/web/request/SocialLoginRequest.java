package mobile.backend.auth.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.auth.domain.command.SocialLoginCommand;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String socialToken;
    private String provider;
    private String socialId;

    public SocialLoginCommand toCommand() {
        return SocialLoginCommand.of(socialToken, provider, socialId);
    }
}
