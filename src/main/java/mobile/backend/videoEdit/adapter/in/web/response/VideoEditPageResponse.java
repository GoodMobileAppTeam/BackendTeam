package mobile.backend.videoEdit.adapter.in.web.response;

import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

import java.util.List;

public record VideoEditPageResponse(
        List<VideoEditResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
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
