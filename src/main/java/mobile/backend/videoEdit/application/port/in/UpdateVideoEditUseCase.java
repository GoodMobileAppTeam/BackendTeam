package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.UpdateVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;

public interface UpdateVideoEditUseCase {
    VideoEdit updateThumbnail(UpdateVideoEditCommand command);
}
