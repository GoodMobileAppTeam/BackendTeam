package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.util.List;

@Schema(description = "영상 목록 응답 DTO (단순 리스트)")
public record VideoEditListResponse(

        @Schema(description = "영상 목록")
        List<VideoEditResponse> content
) {
    public static VideoEditListResponse from(List<VideoEdit> videos) {
        return new VideoEditListResponse(
                videos.stream()
                        .map(VideoEditResponse::from)
                        .toList()
        );
    }
}
