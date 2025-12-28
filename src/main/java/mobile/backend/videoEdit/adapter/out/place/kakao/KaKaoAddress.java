package mobile.backend.videoEdit.adapter.out.place.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * KaKao Reverse GeoCoding  API
 * - 지번 주소 정보 DTO.
 * - road_address의 address_name이 없을 경우 사용
 * </pre>
 *
 * 실제 JSON:
 * <pre><code>
 * "address": {
 *   "address_name": "서울 중구 태평로1가 31"
 * }
 * </code></pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoAddress {

  // 전체 지번 주소
  @JsonProperty("address_name")
  private String addressName;
}
