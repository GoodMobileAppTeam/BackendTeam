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
        String description
) {}

