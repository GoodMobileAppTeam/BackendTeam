package mobile.backend.user.application.service;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.application.port.in.UserCommandUserCase;
import mobile.backend.user.application.port.in.UserQueryUseCase;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserCommandUserCase, UserQueryUseCase {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public User join(UserCreateCommand command) {
    return null;
  }

}
