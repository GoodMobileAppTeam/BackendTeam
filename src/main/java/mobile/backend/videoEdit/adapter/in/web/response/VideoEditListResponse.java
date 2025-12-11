package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "영상 목록 응답 DTO")
public record VideoEditListResponse(

        @Schema(description = "영상 목록")
        List<VideoEditResponse> content,
        LocalDate nextCursorDate,
        Long nextCursorId,
        boolean hasNext
) {
    public static VideoEditListResponse from(List<VideoEdit> videos, int size) {
        boolean hasNext = videos.size() == size;

        LocalDate nextDate = null;
        Long nextId = null;

        if (hasNext) {
            VideoEdit last = videos.get(videos.size() - 1);
            nextDate = last.getSaveTime();
            nextId = last.getId();
        }

        return new VideoEditListResponse(
                videos.stream().map(VideoEditResponse::from).toList(),
                nextDate,
                nextId,
                hasNext
        );
    }
}
