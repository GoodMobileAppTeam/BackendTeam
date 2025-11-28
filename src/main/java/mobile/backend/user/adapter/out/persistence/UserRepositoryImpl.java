package mobile.backend.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.adapter.out.persistence.jpa.UserJpaRepository;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.command.UserCreateCommand;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Repository;
import mobile.backend.user.adapter.out.persistence.entity.UserEntity;
import mobile.backend.user.domain.model.SocialType;
import java.util.Optional;
import mobile.backend.global.exception.CustomException;
import mobile.backend.user.exception.UserErrorCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;

  @Override
  public User create(UserCreateCommand command) {
    return null;
  }

  public Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType) {
    return userJpaRepository.findBySocialIdAndSocialType(socialId, socialType)
            .map(UserEntity::toDomain);
  }

  @Override
  public User save(User user) {
    try {
      UserEntity entity = UserEntity.builder()
              .socialId(user.getSocialId())
              .socialType(user.getSocialType())
              .name(user.getName())
              .profileImageUrl(user.getProfileImageUrl())
              .createdAt(user.getCreatedAt())
              .build();

      UserEntity savedEntity = userJpaRepository.save(entity);
      return savedEntity.toDomain();
    } catch (Exception e) {
      log.error("User 저장 실패: socialId={}, socialType={}", user.getSocialId(), user.getSocialType(), e);
      throw new CustomException(UserErrorCode.USER_SAVE_FAILED);
    }
  }


}
