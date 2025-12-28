package mobile.backend.videoEdit.application.service;

import java.util.List;

public record CursorPageResult<T>(
        List<T> content,
        boolean hasNext,
        boolean hasPrev
) {
}
