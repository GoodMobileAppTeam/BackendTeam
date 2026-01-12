package mobile.backend.videoEdit.adapter.in.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Schema(description = "영상 등록 요청 DTO")
public record CreateVideoEditRequest(
        @NotNull(message = "앨범 ID는 필수입니다.")
        Long albumId,

        @NotBlank(message = "영상 제목은 필수입니다.")
        @Size(max = 10, message = "영상 제목은 최대 10자까지 가능합니다.")
        String title,

        @NotNull(message = "영상 길이는 필수입니다.")
        @Positive(message = "영상 길이는 양수여야 합니다.")
        @Max(value = 60, message = "영상 길이는 60초를 초과할 수 없습니다.")
        Integer duration,

        @NotBlank(message = "영상 URL은 필수입니다.")
        String videoUrl,

        @NotNull(message = "저장 시간은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate userDefinedDate

) {
    public CreateVideoEditCommand toCommand(Long userId, MultipartFile thumbnail) {
        return CreateVideoEditCommand.of(
                albumId,
                userId,
                title,
                duration,
                videoUrl,
                thumbnail,
                thumbnail != null ? thumbnail.getOriginalFilename() : null,
                userDefinedDate
        );
    }
}
