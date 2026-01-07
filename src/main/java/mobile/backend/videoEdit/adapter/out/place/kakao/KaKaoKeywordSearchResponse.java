package mobile.backend.videoEdit.adapter.out.place.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * 카카오 키워드 장소 검색 응답 예시
 *
 * {@code
 * {
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
 *   ],
 *   "meta": {
 *     "is_end": false
 *   }
 * }
 * }
 * </pre>
 */
@Getter
@NoArgsConstructor
public class KaKaoKeywordSearchResponse {
  private List<KaKaoKeywordDocument> documents;
  private Meta meta;

  /**
   *<pre>
   *{@code
   * "meta": {
   *   "is_end": false
   * }
   *
   * false → 다음 페이지 있음
   * true  → 마지막 페이지
   * }
   * </pre>
   */
  @Getter
  @NoArgsConstructor
  public static class Meta {
    @JsonProperty("is_end")
    private boolean isEnd;
  }
}
