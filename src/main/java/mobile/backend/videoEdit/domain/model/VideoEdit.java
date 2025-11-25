package mobile.backend.videoEdit.domain.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VideoEdit {
    private final Long id;
    private final Long albumId;
    private final Long userId;
    private final Integer duration;
    private final String videoUrl;
    private final LocalDate saveTime;
    private boolean isBookMarked;
    private String thumbnailUrl;
    private String description;

    private static final int MAX_DURATION_SECONDS = 60;

    private VideoEdit(Long id, Long albumId, Long userId, Integer duration,
                      String videoUrl, LocalDate saveTime,
                      boolean isBookMarked, String thumbnailUrl, String description) {
        this.id = id;
        this.albumId = albumId;
        this.userId = userId;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.saveTime = saveTime;
        this.isBookMarked = isBookMarked;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }

    // 새 영상 생성용 팩토리 메서드
    public static VideoEdit create(Long albumId, Long userId, Integer duration,
                                   String videoUrl, LocalDate saveTime, String thumbnailUrl, String description) {
        validateDuration(duration);


        return new VideoEdit(
                null, albumId, userId, duration, videoUrl,
                saveTime, false, thumbnailUrl, description
        );
    }

    // DB에서 복원용 팩토리 메서드
    public static VideoEdit reconstitute(Long id, Long albumId, Long userId,
                                         Integer duration, String videoUrl,
                                         LocalDate saveTime,
                                         boolean isBookMarked, String thumbnailUrl, String description) {
        return new VideoEdit(id, albumId, userId, duration, videoUrl,
                saveTime, isBookMarked, thumbnailUrl, description);
    }

    private static void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("영상 길이는 0보다 커야 합니다.");
        }
        if (duration > MAX_DURATION_SECONDS) {
            throw new IllegalArgumentException(
                    String.format("영상 길이는 %d초를 초과할 수 없습니다. 현재: %d초",
                            MAX_DURATION_SECONDS, duration)
            );
        }
    }

    public void toggleBookmark() {
        this.isBookMarked = !this.isBookMarked;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("썸네일 URL은 비어있을 수 없습니다.");
        }
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }

}
