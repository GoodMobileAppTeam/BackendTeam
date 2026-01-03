package mobile.backend.videoEdit.adapter.in.web.response.place;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(title = "PlaceNameSearchResponse : 장소 검색 api 최종 응답값")
public class PlaceNameSearchResponse {

  @Schema(description = "장소 리스트")
  private List<PlaceNameResponse> items;

  @Schema(description = "page(1부터 시작)", example = "1")
  private int page;

  @Schema(description = "size(가져올 개수)", example = "10")
  private int size;

  @Schema(description = "가져올 데이터가 끝났는지", example = "true")
  private boolean end;

  public static PlaceNameSearchResponse from(List<PlaceNameResponse> items, int page, int size, boolean isEnd) {
    return PlaceNameSearchResponse.builder()
        .items(items)
        .page(page)
        .size(size)
        .end(isEnd)
        .build();
  }
}
