package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmListResponse;

public interface BgmQueryUseCase {
  BgmListResponse getBgmList();
}
