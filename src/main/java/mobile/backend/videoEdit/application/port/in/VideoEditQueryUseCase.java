package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.util.List;

public interface VideoEditQueryUseCase {
    VideoEdit getById(Long id, Long userId);
    List<VideoEdit> search(SearchVideoEditCommand command);
    List<VideoEditSummary> getDailySummary(SearchSummaryVideoEditCommand criteria);
}
