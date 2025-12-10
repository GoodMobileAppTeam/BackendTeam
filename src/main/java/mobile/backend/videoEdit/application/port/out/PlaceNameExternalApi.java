package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.domain.command.place.LocationPointCommand;

public interface PlaceNameExternalApi {
  PlaceNameResponse findPlaceName(LocationPointCommand command);
}
