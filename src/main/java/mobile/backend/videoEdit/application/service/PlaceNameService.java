package mobile.backend.videoEdit.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.application.port.in.PlaceNameQueryUseCase;
import mobile.backend.videoEdit.application.port.out.PlaceNameExternalApi;
import mobile.backend.videoEdit.domain.command.place.PlaceNameCommand;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceNameService implements PlaceNameQueryUseCase {

  private final PlaceNameExternalApi externalApi;

  @Override
  public List<PlaceNameResponse> getPlaceNames(PlaceNameCommand command) {
    return command.getLocations().stream()
        .map(externalApi::findPlaceName)
        .toList();
  }
}
