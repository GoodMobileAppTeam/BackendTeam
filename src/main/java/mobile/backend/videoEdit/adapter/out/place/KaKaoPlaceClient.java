package mobile.backend.videoEdit.adapter.out.place;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoAddress;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoReverseDocument;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoReverseGeocodingResponse;
import mobile.backend.videoEdit.adapter.out.place.kakao.KaKaoRoadAddress;
import mobile.backend.videoEdit.application.port.out.PlaceNameExternalApi;
import mobile.backend.videoEdit.domain.command.place.LocationPointCommand;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KaKaoPlaceClient implements PlaceNameExternalApi {

  private final WebClient kakaoWebClient;

  @Override
  public PlaceNameResponse findPlaceName(LocationPointCommand command) {

    // https://dapi.kakao.com/v2/local/geo/coord2address.json?x=126.923728&y=37.556876
    KaKaoReverseGeocodingResponse response = kakaoWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/v2/local/geo/coord2address.json")
            .queryParam("x", command.getLongitude()) // x : 경도(longitude)
            .queryParam("y", command.getLatitude()) // y : 위도(latitude)
            .build())
        .retrieve()
        .bodyToMono(KaKaoReverseGeocodingResponse.class)
        .block();

    String placeName = extractPlaceName(response);

    return PlaceNameResponse.from(command.getLatitude(), command.getLongitude(), placeName);
  }

  private String extractPlaceName(KaKaoReverseGeocodingResponse response) {

    if (response == null || response.getDocuments() == null || response.getDocuments().isEmpty()) {
      throw new CustomException(VideoErrorCode.INVALID_KAKAO_RESPONSE);
    }

    KaKaoReverseDocument document = response.getDocuments().getFirst();

    KaKaoRoadAddress roadAddress = document.getRoadAddress();
    KaKaoAddress address = document.getAddress();

    // 1순위 : 건물명
    if (roadAddress != null && StringUtils.hasText(roadAddress.getBuildingName())) {
      return roadAddress.getBuildingName();
    }

    // 2순위 : 도로명 주소
    if (roadAddress != null && StringUtils.hasText(roadAddress.getAddressName())) {
      return extractRegionPrefix(roadAddress.getAddressName());
    }

    // 3순위 : 지번 주소
    if (address != null && StringUtils.hasText(address.getAddressName())) {
      return extractRegionPrefix(address.getAddressName());
    }

    throw new CustomException(VideoErrorCode.PLACE_NAME_NOT_FOUND);
  }

  /**
   * <pre>
   * 주소에서 시/도 + 구 단위까지만 추출
   * 예 : "서울 중구 태평로1가 31" -> "서울 중구"
   * </pre>
   * @return
   */
  private String extractRegionPrefix(String fullAddress) {
    if (!StringUtils.hasText(fullAddress)) {
      throw new CustomException(VideoErrorCode.PLACE_NAME_NOT_FOUND);
    }

    String[] parts = fullAddress.trim().split("\\s+");

    if (parts.length >= 2) {
      return parts[0] + " " + parts[1]; // 서울 중구
    }
    return parts[0]; // 시/도만 반환
  }
}
