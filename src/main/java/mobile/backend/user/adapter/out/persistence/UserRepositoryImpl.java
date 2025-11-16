package mobile.backend.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.adapter.out.persistence.jpa.UserJpaRepository;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;

  @Override
  public User create(UserCreateCommand command) {
    return null;
  }
}
