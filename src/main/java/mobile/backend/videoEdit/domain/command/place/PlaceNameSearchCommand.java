package mobile.backend.videoEdit.domain.command.place;

import lombok.Getter;

@Getter
public class PlaceNameSearchCommand {

  private final String keyword;
  private final int page;
  private final int size;

  public PlaceNameSearchCommand(String keyword, int page, int size) {
    this.keyword = keyword;
    this.page = page;
    this.size = size;
  }

  public static PlaceNameSearchCommand of(String keyword, int page, int size) {
    return new PlaceNameSearchCommand(keyword, page, size);
  }
}
