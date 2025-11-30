package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface VideoEditRepository {
    VideoEdit save(VideoEdit videoEdit);
    Optional<VideoEdit> findById(Long id);
    Page<VideoEdit> search(SearchVideoEditCommand criteria);
    void delete(VideoEdit videoEdit);
    boolean existsById(Long id);
}
