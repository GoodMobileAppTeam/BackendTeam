package mobile.backend.videoEdit.domain.command;

import mobile.backend.videoEdit.application.service.Cursor;

import java.time.LocalDate;

public record SearchVideoEditCommand(
        Long userId,
        ScrollDirection direction,

        LocalDate baseDateEnd,

        Cursor cursor,

        boolean onlyBookMarked,

        int size
) {
    public static SearchVideoEditCommand init(
            Long userId,
            LocalDate baseDateEnd,
            boolean onlyBookMarked,
            int size
    ) {
        return new SearchVideoEditCommand(
                userId,
                ScrollDirection.INIT,
                baseDateEnd,
                null,
                onlyBookMarked,
                size
        );
    }

    public static SearchVideoEditCommand scroll(
            Long userId,
            ScrollDirection direction,
            Cursor cursor,
            boolean onlyBookMarked,
            int size
    ) {
        return new SearchVideoEditCommand(
                userId,
                direction,
                null,
                cursor,
                onlyBookMarked,
                size
        );
    }
}

