package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "영상 요약 정보 요청 DTO")
public record VideoEditSummaryRequest(

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {
    public SearchSummaryVideoEditCommand toCommand(Long userId) {

        return SearchSummaryVideoEditCommand.of(userId, startDate, endDate);
    }
}
