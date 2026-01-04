package mobile.backend.videoEdit.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameSearchResponse;
import mobile.backend.videoEdit.application.port.in.PlaceNameQueryUseCase;
import mobile.backend.videoEdit.application.port.out.PlaceNameExternalApi;
import mobile.backend.videoEdit.application.port.out.dto.PlaceSearchResult;
import mobile.backend.videoEdit.domain.command.place.PlaceNameSearchCommand;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceNameService implements PlaceNameQueryUseCase {

  private final PlaceNameExternalApi externalApi;


  @Override
  public PlaceNameSearchResponse search(PlaceNameSearchCommand command) {
    PlaceSearchResult result = externalApi.searchByKeyword(command);

    List<PlaceNameResponse> items = result.items().stream()
        .map(place -> PlaceNameResponse.of(place.placeName(), place.address()))
        .toList();

    return PlaceNameSearchResponse.from(
        items,
        command.getPage(),
        command.getSize(),
        result.isEnd()
    );
  }
}
