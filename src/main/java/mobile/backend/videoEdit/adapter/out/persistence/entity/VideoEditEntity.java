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
                        name = "idx_video_user_cursor_desc",
                        columnList = "user_id, is_book_mark, user_defined_date DESC, created_at DESC, id DESC"
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

    @Column(name = "title", nullable = false, length = 10)
    private String title;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "user_defined_date", nullable = false)
    private LocalDate userDefinedDate;

    @Column(name = "is_book_mark", nullable = false)
    private Boolean isBookMark;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;



    private VideoEditEntity(Long id, Long albumId, UserEntity user, String title, Integer duration,
                            String videoUrl, LocalDate userDefinedDate,
                            Boolean isBookMark, String thumbnailUrl) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.user = user;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.userDefinedDate = userDefinedDate;
        this.isBookMark = isBookMark;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static VideoEditEntity from(Long id, Long albumId, Long userId, String title, Integer duration,
                                       String videoUrl,
                                       LocalDate userDefinedDate, Boolean isBookMark,
                                       String thumbnailUrl) {
        UserEntity user = UserEntity.builder().id(userId).build();
        return new VideoEditEntity(id, albumId, user, title, duration, videoUrl,
                        userDefinedDate, isBookMark, thumbnailUrl);
    }


    public static VideoEdit toDomain(VideoEditEntity entity) {
        if (entity == null) return null;

        return VideoEdit.from(
                entity.getId(),
                entity.getAlbumId(),
                entity.getUser().getId(),
                entity.getTitle(),
                entity.getDuration(),
                entity.getVideoUrl(),
                entity.getUserDefinedDate(),
                entity.getCreatedAt(),
                Boolean.TRUE.equals(entity.getIsBookMark()),
                entity.getThumbnailUrl()
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
                domain.getUserDefinedDate(),
                domain.isBookMarked(),
                domain.getThumbnailUrl()
        );
    }
}
