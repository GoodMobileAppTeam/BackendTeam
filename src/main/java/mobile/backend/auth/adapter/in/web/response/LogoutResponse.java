package mobile.backend.auth.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponse {
    private String message;

    public static LogoutResponse of(String message) {
        return LogoutResponse.builder()
                .message(message)
                .build();
    }
}
