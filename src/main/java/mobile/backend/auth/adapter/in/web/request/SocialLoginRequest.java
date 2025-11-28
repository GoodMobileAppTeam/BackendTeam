package mobile.backend.auth.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.auth.domain.command.SocialLoginCommand;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SocialLoginRequest {
    @NotBlank(message = "소셜 토큰은 필수입니다.")
    private String socialToken;
    @NotBlank(message = "Provider는 필수입니다.")
    private String provider;

    public SocialLoginCommand toCommand() {
        return SocialLoginCommand.of(socialToken, provider);
    }
}
