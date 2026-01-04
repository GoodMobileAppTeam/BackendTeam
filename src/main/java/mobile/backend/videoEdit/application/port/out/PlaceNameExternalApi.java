package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.application.port.out.dto.PlaceSearchResult;
import mobile.backend.videoEdit.domain.command.place.PlaceNameSearchCommand;

public interface PlaceNameExternalApi {
  PlaceSearchResult searchByKeyword(PlaceNameSearchCommand command);
}
