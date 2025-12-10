package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.util.List;

public interface VideoEditRepository {
    VideoEdit save(VideoEdit videoEdit);
    VideoEdit findById(Long id);
    List<VideoEdit> search(SearchVideoEditCommand criteria);
    List<VideoEdit> bookmarkSearch(SearchBookmarkVideoEditCommand command);
    void delete(VideoEdit videoEdit);
    List<VideoEditSummary> findDailySummary(SearchVideoEditCommand command);
}
