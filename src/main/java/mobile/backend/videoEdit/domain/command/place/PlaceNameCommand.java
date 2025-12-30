package mobile.backend.videoEdit.domain.command.place;

import java.util.List;
import lombok.Getter;

@Getter
public class PlaceNameCommand {
  private final List<LocationPointCommand> locations;

  public PlaceNameCommand(List<LocationPointCommand> locations) {
    this.locations = locations;
  }

  public static PlaceNameCommand of(List<LocationPointCommand> locations) {
    return new PlaceNameCommand(locations);
  }
}
