package mobile.backend.user.adapter.in.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.user.domain.model.User;

import java.time.LocalDateTime;

@Schema(description = "사용자 정보 응답 DTO")
public record UserResponse(

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "https://bucket.s3.amazonaws.com/profile/uuid.jpg")
        String profileImageUrl,

        @Schema(description = "가입 일시", example = "2024-01-15T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt

) {
    public static UserResponse from(User domain) {
        return new UserResponse(
                domain.getId(),
                domain.getName(),
                domain.getProfileImageUrl(),
                domain.getCreatedAt()
        );
    }
}
