package mobile.backend.videoEdit.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import java.time.LocalDate;

@Schema(description = "영상 검색 요청 DTO")
public record VideoEditSearchRequest(

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate
) {
    public SearchVideoEditCommand toCommand(Long userId) {

        if (startDate.isAfter(endDate)) {
            throw new CustomException(VideoErrorCode.INVALID_DATE_RANGE);
        }

        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new CustomException(VideoErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }

        return SearchVideoEditCommand.of(userId, startDate, endDate);
    }
}
