package mobile.backend.videoEdit.adapter.out.persistence.querydsl;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;

import java.util.List;

public interface VideoEditQuerydslRepository {

    List<VideoEditEntity> search(SearchVideoEditCommand command);

    boolean existsOlder(SearchVideoEditCommand command, VideoEditEntity last);

    boolean existsNewer(SearchVideoEditCommand command, VideoEditEntity first);
}

