package mobile.backend.videoEdit.application.port.out;

import mobile.backend.videoEdit.domain.command.place.LocationPointCommand;

public interface PlaceNameExternalApi {
  String findPlaceName(LocationPointCommand command);
}
