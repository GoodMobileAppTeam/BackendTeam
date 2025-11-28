package mobile.backend.auth.adapter.out.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.auth.application.port.out.SocialTokenValidator;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.global.exception.CustomException;
import mobile.backend.user.domain.model.SocialType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import mobile.backend.user.domain.model.SocialUserInfo;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoTokenValidator implements SocialTokenValidator {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean matchesSocialType(SocialType socialType) {
        return socialType == SocialType.KAKAO;
    }

    @Override
    public SocialUserInfo validateAndGetUserInfo(String accessToken) {

          try {
              // 1. Access Token으로 카카오 사용자 정보 API 호출
              HttpHeaders headers = new HttpHeaders();
              headers.setBearerAuth(accessToken);
              HttpEntity<String> entity = new HttpEntity<>(headers);

              ResponseEntity<String> response = restTemplate.exchange(
                      KAKAO_USER_INFO_URL,
                      HttpMethod.GET,
                      entity,
                      String.class
              );

              // 2. 응답 검증
              if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                  log.error("Kakao API returned non-success status: {}", response.getStatusCode());
                  throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
              }

              // 3. 응답에서 사용자 ID 추출
              JsonNode jsonNode = objectMapper.readTree(response.getBody());

              if (!jsonNode.has("id")) {
                  log.error("Kakao API response missing 'id' field");
                  throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
              }

              String socialId = jsonNode.get("id").asText();
              String nickname = jsonNode.path("kakao_account").path("profile").path("nickname").asText();
              String profileImageUrl = jsonNode.path("kakao_account").path("profile").path("profile_image_url").asText();

              log.info("Kakao token validated successfully for user: {}", socialId);
              return SocialUserInfo.of(socialId, nickname, profileImageUrl);

          } catch (CustomException e) {
              throw e;
          } catch (Exception e) {
              log.error("Kakao token validation failed: {}", e.getMessage(), e);
              throw new CustomException(AuthErrorCode.INVALID_SOCIAL_TOKEN);
          }

    }
}
