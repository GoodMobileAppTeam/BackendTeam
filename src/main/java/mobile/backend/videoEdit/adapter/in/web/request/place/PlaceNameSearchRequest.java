package mobile.backend.videoEdit.adapter.in.web.request.place;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import mobile.backend.videoEdit.domain.command.place.PlaceNameSearchCommand;

@Getter
@Setter
@Schema(title = "PlaceNameSearchRequest : 장소 검색 api 요청 DTO")
public class PlaceNameSearchRequest {

  @NotBlank
  @Schema(description = "키워드(쿼리)", example = "성수")
  private String query;

  @Min(1) @Max(45)
  @Schema(description = "페이지(1부터 시작)", example = "1")
  private Integer page;

  @Min(1) @Max(15)
  @Schema(description = "가져올 개수", example = "10")
  private Integer size;

  public PlaceNameSearchCommand toCommand() {
    return PlaceNameSearchCommand.of(query, page, size);
  }
}
