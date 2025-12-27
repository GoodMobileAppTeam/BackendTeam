package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "영상 목록 응답 DTO")
public record VideoEditListResponse(

        List<VideoEditResponse> content,

        LocalDate nextCursorSaveTime,
        LocalDateTime nextCursorCreatedAt,
        Long nextCursorId,

        boolean hasNext
) {

    public static VideoEditListResponse from(
            List<VideoEdit> videos,
            int size
    ) {
        boolean hasNext = videos.size() == size;

        LocalDate saveTime = null;
        LocalDateTime createdAt = null;
        Long id = null;

        if (hasNext) {
            VideoEdit last = videos.get(videos.size() - 1);
            saveTime = last.getSaveTime();
            createdAt = last.getCreatedAt();
            id = last.getId();
        }

        return new VideoEditListResponse(
                videos.stream().map(VideoEditResponse::from).toList(),
                saveTime,
                createdAt,
                id,
                hasNext
        );
    }
}
