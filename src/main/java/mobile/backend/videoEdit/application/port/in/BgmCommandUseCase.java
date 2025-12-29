package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.domain.command.bgm.SetVideoEditBgmCommand;

public interface BgmCommandUseCase {
  void setBgm(SetVideoEditBgmCommand command);
  void clearBgm(Long videoId, Long userId);
}
