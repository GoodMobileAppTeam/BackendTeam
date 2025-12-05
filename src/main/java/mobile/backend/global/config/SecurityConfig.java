package mobile.backend.global.config;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Security Config 설정
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
        "/v1/auth/login",
        "/v1/auth/refresh"
    };

    private static final String[] PUBLIC_GET_ENDPOINTS = {
        "/admin/**",
        "/v1/notices",
        "/v1/notices/*"
    };

    // PUBLIC_GET_ENDPOINTS랑 겹쳐서 인증이 필요한 엔드포인트
    private static final String[] AUTHENTICATED_GET_ENDPOINTS = {
        "/v1/notices/home"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (JWT 사용으로 인해)
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 활성화 (CorsConfig에서 설정)
                .cors(cors -> {})

                // 세션 사용 안 함 (Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth

                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                    // GET이고 인증 필요한 것들
                    .requestMatchers(HttpMethod.GET, AUTHENTICATED_GET_ENDPOINTS).authenticated()

                    // 공개 GET 엔드포인트들
                    .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()

                    // 모든 메서드 permit
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                    // 그 외 모든 요청은 인증 필요
                    .anyRequest().authenticated()
                )

                // JWT 인증 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


