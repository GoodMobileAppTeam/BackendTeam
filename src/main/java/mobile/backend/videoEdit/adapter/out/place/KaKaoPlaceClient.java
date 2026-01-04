package mobile.backend.videoEdit.adapter.out.place;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoKeywordDocument;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoKeywordSearchResponse;
import mobile.backend.videoEdit.application.port.out.PlaceNameExternalApi;
import mobile.backend.videoEdit.application.port.out.dto.PlaceSearchItem;
import mobile.backend.videoEdit.application.port.out.dto.PlaceSearchResult;
import mobile.backend.videoEdit.domain.command.place.PlaceNameSearchCommand;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KaKaoPlaceClient implements PlaceNameExternalApi {

  private final WebClient kakaoWebClient;


  @Override
  public PlaceSearchResult searchByKeyword(PlaceNameSearchCommand command) {

    KaKaoKeywordSearchResponse response = kakaoWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/v2/local/search/keyword.json")
            .queryParam("query", command.getKeyword()) // 카카오는 query 파라미터명 고정
            .queryParam("page", command.getPage())
            .queryParam("size", command.getSize())
            .build())
        .retrieve()
        .bodyToMono(KaKaoKeywordSearchResponse.class)
        .block();

    if (response == null || response.getDocuments() == null || response.getMeta() == null) {
      throw new CustomException(VideoErrorCode.INVALID_KAKAO_RESPONSE);
    }

    List<PlaceSearchItem> items = response.getDocuments().stream()
        .map(this::toItem)
        .toList();

    return new PlaceSearchResult(items, response.getMeta().isEnd());
  }

  private PlaceSearchItem toItem(KaKaoKeywordDocument document) {
    // 도로명(road_address_name) 우선, 없으면 지번(address_name)
    String address = StringUtils.hasText(document.getRoadAddressName())
        ? document.getRoadAddressName()
        : document.getAddressName();

    return new PlaceSearchItem(document.getPlaceName(), address);
  }
}
