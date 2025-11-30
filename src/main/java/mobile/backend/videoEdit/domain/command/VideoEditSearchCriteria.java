package mobile.backend.videoEdit.domain.command;

public record VideoEditSearchCriteria(
        Long userId,
        Integer year,
        Integer month,
        Boolean isBookMarked,
        int page,
        int size
) {
    public static VideoEditSearchCriteria of(Long userId, Integer year, Integer month,
                                             Boolean isBookMarked, int page, int size) {
        return new VideoEditSearchCriteria(userId, year, month, isBookMarked, page, size);
    }

    public static VideoEditSearchCriteria bookmarked(Long userId, int page, int size) {
        return new VideoEditSearchCriteria(userId, null, null, true, page, size);
    }
}
