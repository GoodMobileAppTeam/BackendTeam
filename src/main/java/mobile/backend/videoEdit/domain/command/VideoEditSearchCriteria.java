package mobile.backend.videoEdit.domain.command;

public record VideoEditSearchCriteria(
        Long userId,
        Integer year,
        Integer month,
        Boolean isBookMarked,
        int page,
        int size
) {
    public VideoEditSearchCriteria {
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        if (page < 0) throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("size는 1~100 사이여야 합니다.");
        }
        if (month != null && (month < 1 || month > 12)) {
            throw new IllegalArgumentException("month는 1~12 사이여야 합니다.");
        }
    }

    public static VideoEditSearchCriteria of(Long userId, Integer year, Integer month,
                                             Boolean isBookMarked, int page, int size) {
        return new VideoEditSearchCriteria(userId, year, month, isBookMarked, page, size);
    }

    public static VideoEditSearchCriteria bookmarked(Long userId, int page, int size) {
        return new VideoEditSearchCriteria(userId, null, null, true, page, size);
    }
}
