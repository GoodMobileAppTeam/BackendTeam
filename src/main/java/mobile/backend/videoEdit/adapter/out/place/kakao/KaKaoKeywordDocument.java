package mobile.backend.videoEdit.adapter.out.place.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 지도 api 응답값 중 documents 배열 응답값
 *
 * <pre>
 * {@code
 *   "documents": [
 *     {
 *       "place_name": "서울숲카페거리",
 *       "road_address_name": "",
 *       "address_name": "서울 성동구 성수동1가 668-11"
 *     },
 *     {
 *       "place_name": "서울숲 방문자센터",
 *       "road_address_name": "서울 성동구 뚝섬로 273",
 *       "address_name": "서울 성동구 성수동1가 685-20"
 *     }
 *   ]
 * }
 * </pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoKeywordDocument {

  /**
   * "place_name": "서울숲카페거리"
   * → 화면 1줄 (장소명)
   */
  @JsonProperty("place_name")
  private String placeName;

  /**
   * "road_address_name": "서울 성동구 뚝섬로 273"
   * → 있으면 주소로 우선 사용
   */
  @JsonProperty("road_address_name")
  private String roadAddressName;

  /**
   * "address_name": "서울 성동구 성수동1가 668-11"
   * → 도로명 주소(road_address_name) 없을 때 사용
   */
  @JsonProperty("address_name")
  private String addressName;


}
