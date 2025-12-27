package mobile.backend.videoEdit.application.service;

import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.exception.VideoErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Cursor(
        LocalDate saveTime,
        LocalDateTime createdAt,
        Long id
) {

    public static Cursor of(
            LocalDate saveTime,
            LocalDateTime createdAt,
            Long id
    ) {
        if (saveTime == null || createdAt == null || id == null) {
            throw new CustomException(VideoErrorCode.INVALID_CURSOR);
        }
        return new Cursor(saveTime, createdAt, id);
    }
}
