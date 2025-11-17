package mobile.backend.user.application.port.out;

import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;

public interface UserRepository {
  User create(UserCreateCommand command);
}
