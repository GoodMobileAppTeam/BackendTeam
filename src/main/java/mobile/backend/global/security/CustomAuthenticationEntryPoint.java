package mobile.backend.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mobile.backend.auth.exception.AuthErrorCode;
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
            throws IOException, ServletException {

        AuthErrorCode errorCode;

        if ("EXPIRED_ACCESS_TOKEN".equals(authException.getMessage())) {
            errorCode = AuthErrorCode.EXPIRED_ACCESS_TOKEN;
        } else {
            errorCode = AuthErrorCode.INVALID_TOKEN;
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
