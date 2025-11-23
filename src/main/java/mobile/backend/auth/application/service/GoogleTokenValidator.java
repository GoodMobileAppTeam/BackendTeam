package mobile.backend.auth.application.service;

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

import java.util.Collections;

@Slf4j
@Component
public class GoogleTokenValidator implements SocialTokenValidator {

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String clientId;

    @Override
    public boolean supports(SocialType socialType) {
        return socialType == SocialType.GOOGLE;
    }

    @Override
    public String validateAndGetSocialId(String idToken) {
          // ===== 테스트용: 실제 검증 건너뛰기 =====
//        log.info("TEST MODE: Skipping Google token validation");
//        return "google_test_user_12345";

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

            log.info("Google token validated successfully for user: {}", socialId);
            return socialId;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Google token validation failed: {}", e.getMessage(), e);
            throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
        }
    }
}
