package mobile.backend.videoEdit.application.port.in;

public interface DeleteVideoEditUseCase {
    void delete(Long id, Long userId);
}
