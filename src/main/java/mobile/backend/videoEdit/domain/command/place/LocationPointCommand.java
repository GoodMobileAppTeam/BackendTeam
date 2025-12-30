package mobile.backend.videoEdit.domain.command.place;

import lombok.Getter;

@Getter
public class LocationPointCommand {
  private final double latitude; // 위도
  private final double longitude; // 경도

  public LocationPointCommand(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static LocationPointCommand of(double latitude, double longitude) {
    return new LocationPointCommand(latitude, longitude);
  }
}
