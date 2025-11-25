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
) {
    public CreateVideoEditCommand {
        if (albumId == null) throw new IllegalArgumentException("albumId는 필수입니다.");
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        if (duration == null) throw new IllegalArgumentException("duration은 필수입니다.");
        if (videoUrl == null || videoUrl.isBlank()) {
            throw new IllegalArgumentException("videoUrl은 필수입니다.");
        }
        if (saveTime == null) throw new IllegalArgumentException("saveTime은 필수입니다.");
        if (thumbnailData == null || thumbnailData.length == 0) {
            throw new IllegalArgumentException("썸네일 데이터는 필수입니다.");
        }
        if (thumbnailData.length > 500) {
            throw new IllegalArgumentException("description이 500자를 초과합니다");
        }
    }
}

