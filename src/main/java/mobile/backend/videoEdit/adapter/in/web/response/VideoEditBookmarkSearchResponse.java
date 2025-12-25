package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "북마크 영상 목록 응답 DTO")
public record  VideoEditBookmarkSearchResponse(
        List<VideoEditResponse> content,
        LocalDate nextCursorDate,
        Long nextCursorId,
        boolean hasNext
) {
    public static VideoEditBookmarkSearchResponse from(List<VideoEdit> list, int size) {
        boolean hasNext = list.size() == size;

        LocalDate nextDate = null;
        Long nextId = null;

        if (hasNext) {
            VideoEdit last = list.get(list.size() - 1);
            nextDate = last.getSaveTime();
            nextId = last.getId();
        }

        return new VideoEditBookmarkSearchResponse(
                list.stream().map(VideoEditResponse::from).toList(),
                nextDate,
                nextId,
                hasNext
        );
    }
}
