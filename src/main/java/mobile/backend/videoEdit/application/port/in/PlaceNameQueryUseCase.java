package mobile.backend.videoEdit.application.port.in;

import java.util.List;
import mobile.backend.videoEdit.domain.command.place.PlaceNameCommand;

public interface PlaceNameQueryUseCase {
  List<String> getPlaceNames(PlaceNameCommand command);
}
