package mobile.backend.videoEdit.adapter.out.place.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * KaKao Reverse GeoCoding API
 * - documents 배열의 각 요소를 표현하는 DTO
 * </pre>
 *
 * 실제 JSON:
 * <pre><code>
 * {
 *   "road_address": {...},
 *   "address": {...}
 * }
 * </code></pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoReverseDocument {

  // 도로명 주소 객체
  @JsonProperty("road_address")
  private KaKaoAddress roadAddress;

  // 지번 주소 객체
  @JsonProperty("address")
  private KaKaoAddress address;

}
