package mobile.backend.videoEdit.adapter.out.place.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * KaKao Reverse GeoCoding  API
 * - 도로명 주소 정보 DTO
 * </pre>
 *
 * 실제 JSON:
 * <pre><code>
 * "road_address": {
 *   "address_name": "서울특별시 중구 세종대로 110",
 *   "building_name": "서울특별시청"
 * }
 * </code></pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoRoadAddress {

  // 전체 도로명 주소
  @JsonProperty("address_name")
  private String addressName;

  // 건물명이 있으면 여기 들어옴. 없으면 빈 문자열("")일 가능성 높음
  @JsonProperty("building_name")
  private String buildingName;

}
