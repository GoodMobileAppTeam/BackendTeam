package mobile.backend.user.application.service;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.application.port.in.UserQueryUseCase;
import mobile.backend.user.application.port.out.TestUserRepository;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserQueryUseCase {

  private final UserRepository userRepository;
  private final TestUserRepository testUserRepository; //테스트 유저 조회

  @Override
  public User getUserInfo(Long userId) {
    return userRepository.findById(userId);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    userRepository.findById(userId);  // 없으면 예외 던wla
    userRepository.deleteById(userId);
  }
}
