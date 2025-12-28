package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Schema(description = "북마크 영상 검색 요청 DTO")
public record VideoEditBookmarkSearchRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate cursorDate,

        Long cursorId,

        @NotNull
        @Min(1)
        @Max(50)
        Integer size
) {
    public SearchBookmarkVideoEditCommand toCommand(Long userId) {
        return new SearchBookmarkVideoEditCommand(userId, cursorDate, cursorId, size);
    }
}
