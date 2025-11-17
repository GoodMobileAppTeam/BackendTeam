package mobile.backend.user.domain.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {

  private static final String PROFILE_IMAGE_PATH = "profile/%s";

  private final Long id;

  private final String name;

  private final String socialId;

  private final SocialType socialType;

  private final LocalDateTime createdAt;

  private User(Long id, String name, String socialId, SocialType socialType,
      LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.socialId = socialId;
    this.socialType = socialType;
    this.createdAt = createdAt;
  }

  public static User of(Long id, String name, String socialId, SocialType socialType, LocalDateTime createdAt) {
    return new User(id, name, socialId, socialType, createdAt);
  }

  public static String getProfileImagePath(String uuid) {
    return String.format(PROFILE_IMAGE_PATH, uuid);
  }


}
