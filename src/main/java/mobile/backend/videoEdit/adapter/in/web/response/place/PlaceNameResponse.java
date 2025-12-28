package mobile.backend.videoEdit.adapter.in.web.response.place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(title = "PlaceNameResponse : 위도/경도와 장소명을 함께 반환하는 최종 응답 DTO")
public class PlaceNameResponse {

  @Schema(description = "위도 (latitude)", example = "37.5665")
  private double latitude;

  @Schema(description = "경도 (longitude)", example = "126.9780")
  private double longitude;

  @Schema(description = "장소명 (건물명 또는 상위 행정구역명)", example = "서울 중구")
  private String placeName;

  public static PlaceNameResponse from(double latitude, double longitude, String placeName) {
    return PlaceNameResponse.builder()
        .latitude(latitude)
        .longitude(longitude)
        .placeName(placeName)
        .build();
  }
}
