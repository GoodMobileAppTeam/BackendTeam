package mobile.backend.global.security.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.auth.exception.JwtAuthenticationException;
import mobile.backend.global.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            // 토큰이 없는 경우 → 인증 없이 다음 필터 진행
            if (!StringUtils.hasText(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 검증 (여기서 예외 발생 가능)
            jwtProvider.validateToken(jwt);

            Long userId = jwtProvider.getUserIdFromToken(jwt);

            CustomUserDetails userDetails = new CustomUserDetails(userId);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        }

        // 토큰의 만료 시간이 지난 경우
        catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        // JWT 형식이 올바르지 않은 경우
        catch (MalformedJwtException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.INVALID_TOKEN);
        }

        //JWT 서명 검증 실패
        catch (SignatureException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.INVALID_TOKEN);
        }

        // 지원하지 않는 JWT 형식
        catch (UnsupportedJwtException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.INVALID_TOKEN);
        }

        // 토큰 값이 null이거나 빈 문자열인 경우
        catch (IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.INVALID_PARAMETER);
        }

        // JWT 처리 중 발생하는 기타 모든 예외의 상위 타입
        catch (JwtException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        // Authorization 헤더에서 추출
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        // Cookie에서 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
