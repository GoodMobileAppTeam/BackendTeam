package mobile.backend.videoEdit.domain.command;

import java.time.LocalDate;

public record SearchVideoEditCommand(
        Long userId,
        LocalDate startDate,
        LocalDate endDate
) {
    public static SearchVideoEditCommand of(Long userId, LocalDate startDate,
                                            LocalDate endDate) {
        return new SearchVideoEditCommand(userId, startDate, endDate);
    }
}
