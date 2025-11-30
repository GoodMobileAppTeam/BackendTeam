package mobile.backend.videoEdit.domain.command;

import java.time.LocalDate;

public record CreateVideoEditCommand(
        Long albumId,
        Long userId,
        Integer duration,
        String videoUrl,
        byte[] thumbnailData,
        String thumbnailFileName,
        LocalDate saveTime,
        String description
) {}

