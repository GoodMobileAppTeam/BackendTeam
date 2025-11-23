package mobile.backend.auth.application.port.out;

import mobile.backend.user.domain.model.SocialType;

public interface SocialTokenValidator {
    boolean supports(SocialType socialType);
    String validateAndGetSocialId(String accessToken);
}