package mobile.backend.videoEdit.adapter.out.place.kakao;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * KaKao Reverse GeoCoding  API
 * - Reverse Geocoding(위도,경도 -> 주소) 응답의 최상위 JSON 구조
 * </pre>
 *
 * 실제 JSON:
 * <pre><code>
 * {
 *   "meta": {...},
 *   "documents": [
 *     {
 *       "road_address": {...},
 *       "address": {...}
 *     }
 *   ]
 * }
 * </code></pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoReverseGeocodingResponse {
  // 주소 결과들이 들어 있는 리스트
  private List<KaKaoReverseDocument> documents;

}
