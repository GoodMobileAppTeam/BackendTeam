package mobile.backend.videoEdit.domain.command;

public record SearchVideoEditCommand(
        Long userId,
        Integer year,
        Integer month,
        Boolean isBookMarked,
        int page,
        int size
) {
    public static SearchVideoEditCommand of(Long userId, Integer year, Integer month,
                                            Boolean isBookMarked, int page, int size) {
        return new SearchVideoEditCommand(userId, year, month, isBookMarked, page, size);
    }

    public static SearchVideoEditCommand bookmarked(Long userId, int page, int size) {
        return new SearchVideoEditCommand(userId, null, null, true, page, size);
    }
}
