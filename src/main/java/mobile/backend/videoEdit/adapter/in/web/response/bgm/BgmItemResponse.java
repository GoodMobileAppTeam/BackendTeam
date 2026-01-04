package mobile.backend.videoEdit.adapter.in.web.response.bgm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(title = "BgmItemResponse : bgm 한개의 응답 DTO")
public class BgmItemResponse {

  private String bgmKey;
  private String title;
  private String artist;
  private Integer durationSec;
  private String thumbnailUrl;
  private String audioUrl;

  public static BgmItemResponse from(String bgmKey, String title, String artist, Integer durationSec, String thumbnailUrl, String audioUrl) {
    return BgmItemResponse.builder()
        .bgmKey(bgmKey)
        .title(title)
        .artist(artist)
        .durationSec(durationSec)
        .thumbnailUrl(thumbnailUrl)
        .audioUrl(audioUrl)
        .build();
  }
}
