package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.util.List;

public interface VideoEditQueryUseCase {
    VideoEdit getById(Long id, Long userId);
    List<VideoEdit> search(SearchVideoEditCommand criteria);
    List<VideoEdit> getBookmarkedVideos(SearchBookmarkVideoEditCommand command);
}
