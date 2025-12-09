package mobile.backend.global.config.kakao;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <pre>
 * KaKao API 전용 WebClient 설정
 *  - baseUrl = http://dapi.kakao.com
 *  - Authorization = KakaoAK {REST_API_KEY}
 * </pre>
 */
@Configuration
public class KaKaoClientConfig {

  @Value("${kakao.local.api-key}")
  private String kakaoAPiKey;

  @Bean
  public WebClient kakaoClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl("https://dapi.kakao.com")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAk " + kakaoAPiKey)
        .build();
  }

}
