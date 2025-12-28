package mobile.backend.videoEdit.adapter.in.web.response.bgm;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "BgmListResponse : bgm 리스트 응답 DTO")
public class BgmListResponse {

  private List<BgmItemResponse> bgm;

  public static BgmListResponse from(List<BgmItemResponse> bgm) {
    return new BgmListResponse(bgm);
  }

}
