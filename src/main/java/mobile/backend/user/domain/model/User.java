package mobile.backend.user.domain.model;

import java.time.LocalDateTime;
import lombok.Getter;


@Getter
public class User {

  private static final String PROFILE_IMAGE_PATH = "profile/%d/%s";

  private final Long id;

  private final String name;

  private final String socialId;

  private final SocialType socialType;

  private final String password;

  private final String profileImageUrl;

  private final LocalDateTime createdAt;


  private User(Long id, String name, String socialId, SocialType socialType, String password, String profileImageUrl, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.socialId = socialId;
    this.socialType = socialType;
    this.password = password;
    this.profileImageUrl = profileImageUrl;
    this.createdAt = createdAt;
  }

  public static User of(Long id, String name, String socialId, SocialType socialType, String password, String profileImageUrl, LocalDateTime createdAt) {
    return new User(id, name, socialId, socialType, password, profileImageUrl, createdAt);
  }

  public static User fromSocialUserInfo(SocialUserInfo userInfo, SocialType socialType) {
    return new User(
            null,  // id는 저장 시 자동 생성
            userInfo.getNickname(),
            userInfo.getSocialId(),
            socialType,
            null,
            userInfo.getProfileImageUrl(),
            LocalDateTime.now()
    );
  }

  public static User createTestUser(String email, String encodedPassword, String name) {
    return new User(
            null,
            name,
            email,  // socialId에 email 저장
            SocialType.TEST,
            encodedPassword,
            null,
            LocalDateTime.now()
    );
  }

  public static String getProfileImagePath(Long userId, String uuid) {
    return String.format(PROFILE_IMAGE_PATH, userId, uuid);
  }

  public User updateName(String newName) {
    return new User(
            this.id,
            newName,
            this.socialId,
            this.socialType,
            this.password,
            this.profileImageUrl,
            this.createdAt
    );
  }
  public User updateProfileImage(String newProfileImageUrl) {
    return new User(
            this.id,
            this.name,
            this.socialId,
            this.socialType,
            this.password,
            newProfileImageUrl,
            this.createdAt
    );
  }
}
