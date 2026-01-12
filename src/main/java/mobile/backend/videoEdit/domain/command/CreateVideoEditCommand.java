package mobile.backend.videoEdit.domain.command;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record CreateVideoEditCommand(
        Long albumId,
        Long userId,
        String title,
        Integer duration,
        String videoUrl,
        MultipartFile thumbnailData,
        String thumbnailFileName,
        LocalDate saveTime
) {
    public static CreateVideoEditCommand of(
            Long albumId,
            Long userId,
            String title,
            Integer duration,
            String videoUrl,
            MultipartFile thumbnailData,
            String thumbnailFileName,
            LocalDate saveTime
    ) {
        return new CreateVideoEditCommand(
                albumId,
                userId,
                title,
                duration,
                videoUrl,
                thumbnailData,
                thumbnailFileName,
                saveTime
        );
    }
}
