package mobile.backend.videoEdit.adapter.in.web.request.place;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import mobile.backend.videoEdit.domain.command.place.PlaceNameCommand;

@Getter
@Schema(title = "PlaceNameListRequest : (위도,경도)들을 묶어서 보내는 요청 DTO")
public class PlaceNameListRequest {

  @Schema(description = "위치(위도,경도) 목록")
  private List<LocationPointRequest> locations;

  public PlaceNameCommand toCommand() {
    return PlaceNameCommand.of(
        locations.stream()
            .map(LocationPointRequest::toCommand)
            .toList()
    );
  }
}
