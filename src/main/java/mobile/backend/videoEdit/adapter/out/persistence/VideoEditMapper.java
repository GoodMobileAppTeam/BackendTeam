package mobile.backend.videoEdit.adapter.out.persistence;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.stereotype.Component;

@Component
public class VideoEditMapper {

    public VideoEdit toDomain(VideoEditEntity entity) {
        if (entity == null) return null;

        return VideoEdit.reconstitute(
                entity.getId(),
                entity.getAlbumId(),
                entity.getUserId(),
                entity.getDuration(),
                entity.getVideoUrl(),
                entity.getSaveTime(),
                Boolean.TRUE.equals(entity.getIsBookMark()),
                entity.getThumbnailUrl()
        );

    }

    public VideoEditEntity toEntity(VideoEdit domain) {
        if (domain == null) return null;

        return VideoEditEntity.from(
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
