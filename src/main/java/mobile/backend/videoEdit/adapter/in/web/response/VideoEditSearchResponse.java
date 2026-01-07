package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.application.service.querymodel.CursorPageResult;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "영상 목록 응답 DTO")
public record VideoEditSearchResponse(

        List<VideoEditResponse> content,

        // NEXT
        LocalDate nextCursorUserDefinedDate,
        LocalDateTime nextCursorCreatedAt,
        Long nextCursorId,
        boolean hasNext,

        // PREV
        LocalDate prevCursorUserDefinedDate,
        LocalDateTime prevCursorCreatedAt,
        Long prevCursorId,
        boolean hasPrev
) {

    public static VideoEditSearchResponse from(CursorPageResult<VideoEdit> page) {

        if (page.content().isEmpty()) {
            return new VideoEditSearchResponse(
                    List.of(),
                    null, null, null, false,
                    null, null, null, false
            );
        }

        VideoEdit first = page.content().get(0);
        VideoEdit last  = page.content().get(page.content().size() - 1);

        return new VideoEditSearchResponse(
                page.content().stream()
                        .map(VideoEditResponse::from)
                        .toList(),

                page.hasNext() ? last.getUserDefinedDate() : null,
                page.hasNext() ? last.getCreatedAt() : null,
                page.hasNext() ? last.getId() : null,
                page.hasNext(),

                page.hasPrev() ? first.getUserDefinedDate() : null,
                page.hasPrev() ? first.getCreatedAt() : null,
                page.hasPrev() ? first.getId() : null,
                page.hasPrev()
        );
    }
}
