package mobile.backend.user.application.port.in;

import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;

public interface UserCommandUserCase {
  User join(UserCreateCommand command);

}
