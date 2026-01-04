package mobile.backend.videoEdit.domain.command.bgm;

import lombok.Getter;

@Getter
public class SetVideoEditBgmCommand {

  private final Long videoId;
  private final Long userId;
  private final String bgmKey;

  public SetVideoEditBgmCommand(Long videoId, Long userId, String bgmKey) {
    this.videoId = videoId;
    this.userId = userId;
    this.bgmKey = bgmKey;
  }

  public static SetVideoEditBgmCommand of(Long videoId, Long userId, String bgmKey) {
    return new SetVideoEditBgmCommand(videoId, userId, bgmKey);
  }
}
