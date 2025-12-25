package mobile.backend.videoEdit.domain.command;

import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.exception.VideoErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SearchVideoEditCommand(
        Long userId,

        // INIT 전용
        LocalDate baseDateEnd,

        // 커서 전용
        LocalDate cursorSaveTime,
        LocalDateTime cursorCreatedAt,
        Long cursorId,

        ScrollDirection direction,
        int size
) {

    /* ================= INIT ================= */

    public static SearchVideoEditCommand init(
            Long userId,
            LocalDate baseDateEnd,
            int size
    ) {
        if (baseDateEnd.isAfter(LocalDate.now())) {
            throw new CustomException(VideoErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }

        return new SearchVideoEditCommand(
                userId,
                baseDateEnd,
                null,
                null,
                null,
                ScrollDirection.INIT,
                size
        );
    }

    /* ================= SCROLL ================= */

    public static SearchVideoEditCommand scroll(
            Long userId,
            LocalDate cursorSaveTime,
            LocalDateTime cursorCreatedAt,
            Long cursorId,
            ScrollDirection direction,
            int size
    ) {
        if (direction == ScrollDirection.INIT) {
            throw new IllegalArgumentException("INIT에는 커서를 사용할 수 없습니다.");
        }

        if (cursorSaveTime == null || cursorCreatedAt == null || cursorId == null) {
            throw new IllegalArgumentException("스크롤 조회에는 커서 정보가 필요합니다.");
        }

        return new SearchVideoEditCommand(
                userId,
                null,
                cursorSaveTime,
                cursorCreatedAt,
                cursorId,
                direction,
                size
        );
    }
}


