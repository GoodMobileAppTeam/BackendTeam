package mobile.backend.user.application.service;

import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import mobile.backend.user.application.port.in.UserQueryUseCase;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserQueryUseCase {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public User getUserInfo(Long userId) {
    return userRepository.findById(userId);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    userRepository.findById(userId);  // 없으면 예외 던짐
    refreshTokenRepository.deleteByUserId(userId); // Redis RefreshToken 수동 삭제
    userRepository.deleteById(userId);
  }
}
