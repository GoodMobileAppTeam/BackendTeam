package mobile.backend.auth.domain.command;

import lombok.Builder;
import lombok.Getter;
import mobile.backend.user.domain.model.SocialType;

@Getter
@Builder
public class SocialLoginCommand {
    private String socialToken;
    private SocialType provider;
    private String socialId;

    public static SocialLoginCommand of(String socialToken, String provider, String socialId) {
        return SocialLoginCommand.builder()
                .socialToken(socialToken)
                .provider(SocialType.valueOf(provider.toUpperCase()))
                .socialId(socialId)
                .build();
    }
}