package mobile.backend.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mobile.backend.auth.exception.AuthErrorCode;
import mobile.backend.auth.exception.JwtAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        AuthErrorCode errorCode = AuthErrorCode.AUTHENTICATION_FAILED;

        if (authException instanceof JwtAuthenticationException jwtEx) {
            errorCode = jwtEx.getErrorCode();
        }

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = Map.of(
                "status", errorCode.getStatus().value(),
                "message", errorCode.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
