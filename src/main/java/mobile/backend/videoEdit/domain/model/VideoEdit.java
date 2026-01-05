package mobile.backend.videoEdit.domain.model;

import lombok.Getter;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.exception.VideoErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class VideoEdit {
    private final Long id;
    private final Long albumId;
    private final Long userId;
    private final String title;
    private final Integer duration;
    private final String videoUrl;
    private final LocalDate userDefinedDate;
    private final LocalDateTime createdAt;
    private boolean isBookMarked;
    private String thumbnailUrl;

    private String bgmKey;

    private static final int MAX_DURATION_SECONDS = 60;

    private VideoEdit(Long id, Long albumId, Long userId, String title, Integer duration,
                      String videoUrl, LocalDate userDefinedDate, LocalDateTime createdAt,
                      boolean isBookMarked, String thumbnailUrl, String bgmKey) {
        this.id = id;
        this.albumId = albumId;
        this.userId = userId;
        this.title = title;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.userDefinedDate = userDefinedDate;
        this.createdAt = createdAt;
        this.isBookMarked = isBookMarked;
        this.thumbnailUrl = thumbnailUrl;
        this.bgmKey = bgmKey;
    }

    // 새 영상 생성용 팩토리 메서드
    public static VideoEdit create(Long albumId, Long userId, String title, Integer duration,
                                   String videoUrl, LocalDate userDefinedDate, String thumbnailUrl, String bgmKey) {
        validateDuration(duration);


        return new VideoEdit(
                null, albumId, userId, title, duration, videoUrl,
                userDefinedDate, null, false, thumbnailUrl, bgmKey
        );
    }

    // DB에서 복원용 팩토리 메서드
    public static VideoEdit from(Long id, Long albumId, Long userId, String title,
                                         Integer duration, String videoUrl,
                                         LocalDate userDefinedDate, LocalDateTime createdAt,
                                         boolean isBookMarked, String thumbnailUrl, String bgmKey) {
        return new VideoEdit(id, albumId, userId, title, duration, videoUrl,
                userDefinedDate, createdAt, isBookMarked, thumbnailUrl, bgmKey);
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

    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }

    public static void validateOwnership(VideoEdit videoEdit, Long userId) {
        if (!videoEdit.isOwnedBy(userId)) {
            throw new CustomException(VideoErrorCode.VIDEO_ACCESS_DENIED);
        }
    }
}
