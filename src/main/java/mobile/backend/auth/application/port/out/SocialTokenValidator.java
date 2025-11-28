package mobile.backend.auth.application.port.out;

import mobile.backend.user.domain.model.SocialType;
import mobile.backend.user.domain.model.SocialUserInfo;

public interface SocialTokenValidator {
    boolean matchesSocialType(SocialType socialType);
    SocialUserInfo validateAndGetUserInfo(String token);
}