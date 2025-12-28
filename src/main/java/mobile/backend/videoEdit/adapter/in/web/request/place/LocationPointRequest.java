package mobile.backend.videoEdit.adapter.in.web.request.place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import mobile.backend.videoEdit.domain.command.place.LocationPointCommand;

@Getter
@Schema(title = "LocationPointRequest : 위도,경도 요청 DTO")
public class LocationPointRequest {

  @Schema(description = "위도 (latitude)", example = "37.5665")
  private double latitude;

  @Schema(description = "경도 (longitude)", example = "126.9780")
  private double longitude;

  public LocationPointCommand toCommand() {
    return LocationPointCommand.of(latitude, longitude);
  }

}
