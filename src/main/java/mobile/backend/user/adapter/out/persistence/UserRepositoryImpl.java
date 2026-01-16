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

  @Override
  public User findById(Long id) {
    return userJpaRepository.findById(id)
            .map(UserEntity::toDomain)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
  }

  @Override
  public boolean existsBySocialIdAndSocialType(String socialId, SocialType socialType) {
    return userJpaRepository.existsBySocialIdAndSocialType(socialId, socialType);
  }

  @Override
  public void deleteById(Long id) {
    userJpaRepository.deleteById(id);
  }

  public User update(User user) {
    try {
      UserEntity existingEntity = userJpaRepository.findById(user.getId())
              .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

      // 업데이트할 필드만 변경
      UserEntity updatedEntity = UserEntity.builder()
              .id(existingEntity.getId())
              .name(user.getName())
              .socialId(existingEntity.getSocialId())
              .socialType(existingEntity.getSocialType())
              .password(existingEntity.getPassword())
              .profileImageUrl(user.getProfileImageUrl())
              .createdAt(existingEntity.getCreatedAt())
              .build();

      UserEntity savedEntity = userJpaRepository.save(updatedEntity);
      return savedEntity.toDomain();
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(UserErrorCode.USER_SAVE_FAILED);
    }
  }

}
