package mobile.backend.videoEdit.domain.command;

import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.exception.VideoErrorCode;

import java.time.LocalDate;

public record SearchVideoEditCommand(
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate cursorDate,
        Long cursorId,
        int size
) {
    public static SearchVideoEditCommand of(Long userId, LocalDate startDate, LocalDate endDate,
                                            LocalDate cursorDate, Long cursorId, int size) {

        if (startDate.isAfter(endDate)) {
            throw new CustomException(VideoErrorCode.INVALID_DATE_RANGE);
        }

        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new CustomException(VideoErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }

        return new SearchVideoEditCommand(userId, startDate, endDate, cursorDate, cursorId, size);
    }
}
