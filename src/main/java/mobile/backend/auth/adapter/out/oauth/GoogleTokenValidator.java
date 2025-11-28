package mobile.backend.auth.adapter.out.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.application.port.out.SocialTokenValidator;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;
import mobile.backend.user.domain.model.SocialType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import mobile.backend.user.domain.model.SocialUserInfo;

import java.util.Collections;

@Slf4j
@Component
public class GoogleTokenValidator implements SocialTokenValidator {

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String clientId;

    @Override
    public boolean matchesSocialType(SocialType socialType) {
        return socialType == SocialType.GOOGLE;
    }

    @Override
    public SocialUserInfo validateAndGetUserInfo(String idToken) {

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                log.error("Google idToken verification failed: token is invalid");
                throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String socialId = payload.getSubject();
            String nickname = (String) payload.get("name");
            String profileImageUrl = (String) payload.get("picture");
            log.info("Google token validated successfully for user: {}", socialId);
            return SocialUserInfo.of(socialId, nickname, profileImageUrl);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Google token validation failed: {}", e.getMessage(), e);
            throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
        }
    }
}
