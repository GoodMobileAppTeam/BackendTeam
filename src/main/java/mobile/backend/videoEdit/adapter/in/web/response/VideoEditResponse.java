package mobile.backend.videoEdit.adapter.in.web.response;

import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;

public record VideoEditResponse(
        Long id,
        Long albumId,
        Long userId,
        Integer duration,
        String videoUrl,
        LocalDate saveTime,
        boolean isBookMarked,
        String thumbnailUrl
) {
    public static VideoEditResponse from(VideoEdit domain) {
        return new VideoEditResponse(
                domain.getId(),
                domain.getAlbumId(),
                domain.getUserId(),
                domain.getDuration(),
                domain.getVideoUrl(),
                domain.getSaveTime(),
                domain.isBookMarked(),
                domain.getThumbnailUrl()
        );
    }
}
