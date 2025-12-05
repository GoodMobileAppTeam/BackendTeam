package mobile.backend.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfo {
    private String socialId;
    private String nickname;
    private String profileImageUrl;

    public static SocialUserInfo of(String socialId, String nickname, String profileImageUrl) {
        return SocialUserInfo.builder()
                .socialId(socialId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
