package mobile.backend.videoEdit.domain.command;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record CreateVideoEditCommand(
        Long albumId,
        Long userId,
        Integer duration,
        String videoUrl,
        MultipartFile thumbnailData,
        String thumbnailFileName,
        LocalDate saveTime,
        String description,
        String bgmKey
) {
    public static CreateVideoEditCommand of(
            Long albumId,
            Long userId,
            Integer duration,
            String videoUrl,
            MultipartFile thumbnailData,
            String thumbnailFileName,
            LocalDate saveTime,
            String description,
            String bgmKey
    ) {
        return new CreateVideoEditCommand(
                albumId,
                userId,
                duration,
                videoUrl,
                thumbnailData,
                thumbnailFileName,
                saveTime,
                description,
                bgmKey
        );
    }
}

