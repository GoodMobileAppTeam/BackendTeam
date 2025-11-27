package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.UpdateVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;

public interface VideoEditCommandUseCase {
    VideoEdit create(CreateVideoEditCommand command);
    VideoEdit toggle(Long videoEditId, Long userId);
    VideoEdit updateThumbnail(UpdateVideoEditCommand command);
    void delete(Long id, Long userId);
}
