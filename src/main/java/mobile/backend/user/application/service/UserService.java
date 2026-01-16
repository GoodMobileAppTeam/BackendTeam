package mobile.backend.user.application.service;

import mobile.backend.auth.application.port.out.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import mobile.backend.user.application.port.in.UserQueryUseCase;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserQueryUseCase {

  private final AmazonS3Manager amazonS3Manager;
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

  @Override
  @Transactional
  public User updateUserProfile(Long userId, String name, MultipartFile profileImage) {
    // 1. 기존 사용자 조회
    User existingUser = userRepository.findById(userId);
    User updatedUser = existingUser;

    // 2. 이름 업데이트 (name이 null이 아닌 경우에만)
    if (name != null && !name.isBlank()) {
      updatedUser = updatedUser.updateName(name);
    }

    // 3. 프로필 이미지 업데이트 (profileImage가 null이 아닌 경우에만)
    if (profileImage != null && !profileImage.isEmpty()) {
      // 기존 프로필 이미지가 S3 URL이면 삭제
      if (existingUser.getProfileImageUrl() != null && !existingUser.getProfileImageUrl().isBlank()) {
        if (isUserUploadedImage(existingUser.getProfileImageUrl(), userId)) {
          amazonS3Manager.deleteObjectByUrl(existingUser.getProfileImageUrl());
        }
      }

      // 새 이미지 S3 업로드
      String uuid = java.util.UUID.randomUUID().toString();
      String keyName = User.getProfileImagePath(userId, uuid);
      String newProfileImageUrl = amazonS3Manager.uploadFile(keyName, profileImage);

      updatedUser = updatedUser.updateProfileImage(newProfileImageUrl);
    }

    // 4. DB 저장 및 반환
    return userRepository.update(updatedUser);
  }

  private boolean isUserUploadedImage(String url, Long userId) {
    return url != null && url.contains("amazonaws.com") && url.contains("profile/" + userId);
  }

}