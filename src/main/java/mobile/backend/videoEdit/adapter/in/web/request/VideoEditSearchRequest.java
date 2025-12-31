package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import mobile.backend.videoEdit.application.service.Cursor;
import mobile.backend.videoEdit.domain.command.ScrollDirection;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "영상 검색 요청 DTO")
public record VideoEditSearchRequest(

        @NotNull
        ScrollDirection direction,

        // INIT
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate baseDateEnd,

        // CURSOR
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate cursorSaveTime,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime cursorCreatedAt,

        Long cursorId,

        @Min(1)
        @Max(50)
        int size
) {

    public SearchVideoEditCommand toCommand(Long userId, boolean bookMarkApi) {

        return switch (direction) {
            case INIT -> SearchVideoEditCommand.init(
                    userId,
                    baseDateEnd,
                    bookMarkApi,
                    size
            );

            case DOWN, UP -> SearchVideoEditCommand.scroll(
                    userId,
                    direction,
                    Cursor.of(cursorSaveTime, cursorCreatedAt, cursorId),
                    bookMarkApi,
                    size
            );
        };
    }
}


