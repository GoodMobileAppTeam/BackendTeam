package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;

public interface CreateVideoEditUseCase {
    VideoEdit create(CreateVideoEditCommand command);
}
