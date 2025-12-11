package mobile.backend.videoEdit.domain.command;

import java.time.LocalDate;

public record  SearchBookmarkVideoEditCommand(
        Long userId,
        LocalDate cursorDate,
        Long cursorId,
        int size
) {
    public static SearchBookmarkVideoEditCommand of(
            Long userId,
            LocalDate cursorDate,
            Long cursorId,
            int size
    ) {
        return new SearchBookmarkVideoEditCommand(
                userId,
                cursorDate,
                cursorId,
                size
        );
    }
}
