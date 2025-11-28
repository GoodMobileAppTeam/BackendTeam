package mobile.backend.user.application.port.out;

import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;
import mobile.backend.user.domain.model.SocialType;
import java.util.Optional;

public interface UserRepository {
  User create(UserCreateCommand command);

  Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);
  User save(User user);

}
