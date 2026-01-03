package mobile.backend.videoEdit.adapter.out.persistence.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import mobile.backend.user.adapter.out.persistence.entity.UserEntity;
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
                        name = "idx_video_user_save_time_desc",
                        columnList = "user_id, save_time DESC"
                ),
                @Index(
                        name = "idx_user_save_created_desc",
                        columnList = "user_id, save_time, created_at DESC"
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_video_edit_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            ))
    private UserEntity user;

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

    @Column(name = "description", length = 500)
    private String description;

    private VideoEditEntity(Long id, Long albumId, UserEntity user, Integer duration,
                            String videoUrl, LocalDate saveTime,
                            Boolean isBookMark, String thumbnailUrl, String description) {
        this.id = id;
        this.albumId = albumId;
        this.user = user;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.saveTime = saveTime;
        this.isBookMark = isBookMark;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }

    public static VideoEditEntity from(Long id, Long albumId, Long userId, Integer duration,
                                       String videoUrl,
                                       LocalDate saveTime, Boolean isBookMark,
                                       String thumbnailUrl, String description) {
        UserEntity user = UserEntity.builder().id(userId).build();
        return new VideoEditEntity(id, albumId, user, duration, videoUrl,
                saveTime, isBookMark, thumbnailUrl, description);
    }


    public static VideoEdit toDomain(VideoEditEntity entity) {
        if (entity == null) return null;

        return VideoEdit.from(
                entity.getId(),
                entity.getAlbumId(),
                entity.getUser().getId(),
                entity.getDuration(),
                entity.getVideoUrl(),
                entity.getSaveTime(),
                Boolean.TRUE.equals(entity.getIsBookMark()),
                entity.getThumbnailUrl(),
                entity.getDescription()
        );

    }

    public static VideoEditEntity toEntity(VideoEdit domain) {
        if (domain == null) return null;

        return VideoEditEntity.from(
                domain.getId(),
                domain.getAlbumId(),
                domain.getUserId(),
                domain.getDuration(),
                domain.getVideoUrl(),
                domain.getSaveTime(),
                domain.isBookMarked(),
                domain.getThumbnailUrl(),
                domain.getDescription()
        );
    }
}
