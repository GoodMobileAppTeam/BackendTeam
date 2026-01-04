package mobile.backend.videoEdit.application.port.out.dto;

import java.util.List;

public record PlaceSearchResult (
    List<PlaceSearchItem> items,
    boolean isEnd
) {}
