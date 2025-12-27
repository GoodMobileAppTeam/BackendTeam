package mobile.backend.videoEdit.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.global.adapter.out.persistence.entity.BaseTimeEntity;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;

@Entity
@Table(
        name = "video_edit",
        indexes = {
                @Index(
                        name = "idx_video_user_cursor_desc",
                        columnList = "user_id, is_book_mark, save_time DESC, created_at DESC, id DESC"
                )
        }
)@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoEditEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false, length = 10)
    private String title;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "save_time", nullable = false)
    private LocalDate saveTime;

    @Column(name = "is_book_mark", nullable = false)
    private Boolean isBookMark;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "bgm_key")
    private String bgmKey;

    private VideoEditEntity(Long id, Long albumId, Long userId, Integer duration,
                            String videoUrl, LocalDate saveTime,
                            Boolean isBookMark, String thumbnailUrl, String bgmKey) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.userId = userId;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.saveTime = saveTime;
        this.isBookMark = isBookMark;
        this.thumbnailUrl = thumbnailUrl;
        this.bgmKey = bgmKey;
    }

    public static VideoEditEntity from(Long id, Long albumId, Long userId, String title, Integer duration,
                                       String videoUrl,
                                       LocalDate saveTime, Boolean isBookMark,
                                       String thumbnailUrl, String bgmKey) {
        return new VideoEditEntity(id, albumId, userId, title, duration, videoUrl,
                saveTime, isBookMark, thumbnailUrl, bgmKey);
    }

    public static VideoEdit toDomain(VideoEditEntity entity) {
        if (entity == null) return null;

        return VideoEdit.from(
                entity.getId(),
                entity.getAlbumId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getDuration(),
                entity.getVideoUrl(),
                entity.getSaveTime(),
                entity.getCreatedAt(),
                Boolean.TRUE.equals(entity.getIsBookMark()),
                entity.getThumbnailUrl(),
                entity.getBgmKey()
        );

    }

    public static VideoEditEntity toEntity(VideoEdit domain) {
        if (domain == null) return null;

        return VideoEditEntity.from(
                domain.getId(),
                domain.getAlbumId(),
                domain.getUserId(),
                domain.getTitle(),
                domain.getDuration(),
                domain.getVideoUrl(),
                domain.getSaveTime(),
                domain.isBookMarked(),
                domain.getThumbnailUrl(),
                domain.getBgmKey()
        );
    }
}
