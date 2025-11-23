package mobile.backend.auth.application.port.in;

public interface LogoutUseCase {
    void execute(Long userId);
}
