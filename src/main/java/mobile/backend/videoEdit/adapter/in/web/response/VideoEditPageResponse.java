package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "영상 페이징 응답 DTO")
public record VideoEditPageResponse(
        @Schema(description = "영상 목록")
        List<VideoEditResponse> content,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지 크기", example = "20")
        int size,

        @Schema(description = "전체 영상 개수", example = "100")
        long totalElements,

        @Schema(description = "전체 페이지 개수", example = "5")
        int totalPages,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "이전 페이지 존재 여부", example = "false")
        boolean hasPrevious
) {
    public static VideoEditPageResponse from(Page<VideoEdit> page) {
        List<VideoEditResponse> content = page.getContent()
                .stream()
                .map(VideoEditResponse::from)
                .toList();

        return new VideoEditPageResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
