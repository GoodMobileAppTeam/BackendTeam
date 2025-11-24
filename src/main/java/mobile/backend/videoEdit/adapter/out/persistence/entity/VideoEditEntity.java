package mobile.backend.videoEdit.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.global.adapter.out.persistence.entity.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_edit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoEditEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "save_time", nullable = false)
    private LocalDate saveTime;

    @Column(name = "is_book_mark")
    private Boolean isBookMark;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private VideoEditEntity(Long id, Long albumId, Long userId, Integer duration,
                            String videoUrl, LocalDate saveTime,
                            Boolean isBookMark, String thumbnailUrl) {
        this.id = id;
        this.albumId = albumId;
        this.userId = userId;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.saveTime = saveTime;
        this.isBookMark = isBookMark;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static VideoEditEntity from(Long id, Long albumId, Long userId, Integer duration,
                                       String videoUrl,
                                       LocalDate saveTime, Boolean isBookMark,
                                       String thumbnailUrl) {
        return new VideoEditEntity(id, albumId, userId, duration, videoUrl,
                saveTime, isBookMark, thumbnailUrl);
    }
}
