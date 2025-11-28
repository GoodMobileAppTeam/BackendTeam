package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.VideoEditSearchCriteria;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

public interface VideoEditQueryUseCase {
    VideoEdit getById(Long id, Long userId);
    Page<VideoEdit> search(VideoEditSearchCriteria criteria);
    Page<VideoEdit> getBookmarkedVideos(Long userId, int page, int size);
}
