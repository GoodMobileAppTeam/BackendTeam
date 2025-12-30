package mobile.backend.videoEdit.application.port.in;

import java.util.List;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.domain.command.place.PlaceNameCommand;

public interface PlaceNameQueryUseCase {
  List<PlaceNameResponse> getPlaceNames(PlaceNameCommand command);
}
