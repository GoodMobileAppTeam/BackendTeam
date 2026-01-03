package mobile.backend.videoEdit.application.port.in;

import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameSearchResponse;
import mobile.backend.videoEdit.domain.command.place.PlaceNameSearchCommand;

public interface PlaceNameQueryUseCase {
  PlaceNameSearchResponse search(PlaceNameSearchCommand command);
}
