package mobile.backend.videoEdit.adapter.in.web.response.place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceNameResponse {

  @Schema(description = "장소명", example = "성수역")
  private String placeName;

  @Schema(description = "주소(도로명 우선, 없으면 지번)", example = "서울 성동구 뚝섬로 273")
  private String address;

  public static PlaceNameResponse of(String placeName, String address) {
    return new PlaceNameResponse(placeName, address);
  }

}
