package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import mobile.backend.videoEdit.domain.command.ScrollDirection;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "영상 검색 요청 DTO")
public record VideoEditSearchRequest(

        @NotNull
        ScrollDirection direction,

        // INIT 전용
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate baseDateEnd,

        // 커서
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate cursorSaveTime,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime cursorCreatedAt,

        Long cursorId,

        @NotNull
        @Min(1)
        @Max(50)
        Integer size
) {

    public SearchVideoEditCommand toCommand(Long userId) {

        return switch (direction) {
            case INIT -> SearchVideoEditCommand.init(
                    userId,
                    baseDateEnd,
                    size
            );
            case DOWN, UP -> SearchVideoEditCommand.scroll(
                    userId,
                    cursorSaveTime,
                    cursorCreatedAt,
                    cursorId,
                    direction,
                    size
            );
        };
    }
}

