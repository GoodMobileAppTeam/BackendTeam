package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.application.service.querymodel.CursorPageResult;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.util.List;

public interface VideoEditQueryUseCase {
    List<VideoEdit> getDailyVideos(Long userId, LocalDate userDefinedDate);
    CursorPageResult<VideoEdit> search(SearchVideoEditCommand command);
    List<VideoEditSummary> getDailySummary(SearchSummaryVideoEditCommand criteria);
}
