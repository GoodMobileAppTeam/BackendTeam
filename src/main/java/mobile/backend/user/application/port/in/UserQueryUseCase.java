package mobile.backend.user.application.port.in;

import mobile.backend.user.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserQueryUseCase {
    User getUserInfo(Long userId);
    void deleteUser(Long userId);
    User updateUserProfile(Long userId, String name, MultipartFile profileImage);
}
