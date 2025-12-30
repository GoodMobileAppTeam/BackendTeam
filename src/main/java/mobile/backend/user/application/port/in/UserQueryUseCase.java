package mobile.backend.user.application.port.in;

import mobile.backend.user.domain.model.User;

public interface UserQueryUseCase {
    User getUserInfo(Long userId);
    void deleteUser(Long userId);
}
