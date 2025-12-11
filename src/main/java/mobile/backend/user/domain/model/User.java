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

  private final String profileImageUrl;

  private final LocalDateTime createdAt;


  private User(Long id, String name, String socialId, SocialType socialType, String profileImageUrl, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.socialId = socialId;
    this.socialType = socialType;
    this.profileImageUrl = profileImageUrl;
    this.createdAt = createdAt;
  }

  public static User of(Long id, String name, String socialId, SocialType socialType, String profileImageUrl, LocalDateTime createdAt) {
    return new User(id, name, socialId, socialType, profileImageUrl, createdAt);
  }

  public static User fromSocialUserInfo(SocialUserInfo userInfo, SocialType socialType) {
    return new User(
            null,  // id는 저장 시 자동 생성
            userInfo.getNickname(),
            userInfo.getSocialId(),
            socialType,
            userInfo.getProfileImageUrl(),
            LocalDateTime.now()
    );
  }


  public static String getProfileImagePath(String uuid) {
    return String.format(PROFILE_IMAGE_PATH, uuid);
  }

}
