package mobile.backend.videoEdit.adapter.in.web.request.bgm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import mobile.backend.videoEdit.domain.command.bgm.SetVideoEditBgmCommand;

@Getter
@Schema(title = "VideoEditBgmSetRequest : 배경음악 설정 요청 DTO")
public class SetVideoEditBgmRequest {

  @Schema(description = "배경음악 S3 Key")
  @NotBlank
  private String bgmKey;

  public SetVideoEditBgmCommand toCommand(Long videoId, Long userId) {
    return new SetVideoEditBgmCommand(videoId, userId, bgmKey);
  }

}
