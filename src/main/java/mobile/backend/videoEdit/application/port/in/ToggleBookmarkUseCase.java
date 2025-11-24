package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.model.VideoEdit;

public interface ToggleBookmarkUseCase {
    VideoEdit toggle(Long videoEditId, Long userId);
}
