package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

public interface VideoEditRepository {
    VideoEdit save(VideoEdit videoEdit);
    VideoEdit findById(Long id);
    Page<VideoEdit> search(SearchVideoEditCommand criteria);
    void delete(VideoEdit videoEdit);
    boolean existsById(Long id);
}
