package mobile.backend.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.adapter.out.persistence.jpa.UserJpaRepository;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import mobile.backend.user.adapter.out.persistence.entity.UserEntity;
import mobile.backend.user.domain.model.SocialType;
import java.util.Optional;
import mobile.backend.global.exception.CustomException;
import mobile.backend.user.exception.UserErrorCode;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;

  @Override
  public Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType) {
    return userJpaRepository.findBySocialIdAndSocialType(socialId, socialType)
            .map(UserEntity::toDomain);
  }


  @Override
  public User save(User user) {
    try {
      UserEntity entity = UserEntity.from(user);
      UserEntity savedEntity = userJpaRepository.save(entity);

      return savedEntity.toDomain();
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(UserErrorCode.USER_SAVE_FAILED);
    }
  }
}
