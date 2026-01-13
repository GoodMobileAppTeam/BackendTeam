package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.application.service.querymodel.CursorPageResult;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditRepository {
    VideoEdit save(VideoEdit videoEdit);
    List<VideoEdit> getDailyVideos(Long userId, LocalDate userDefinedDate);
    VideoEdit findById(Long id);
    CursorPageResult<VideoEdit> search(SearchVideoEditCommand command);
    void delete(VideoEdit videoEdit);
    List<VideoEditSummary> findDailySummary(SearchSummaryVideoEditCommand command);
}
