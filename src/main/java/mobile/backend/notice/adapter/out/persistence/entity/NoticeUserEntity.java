package mobile.backend.notice.adapter.out.persistence.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "notice_user",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_notice_user", columnNames = {"userId", "noticeId"})
    }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false,
          foreignKey = @ForeignKey(
                  name = "fk_notice_user_user",
                  foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE"
          ))
  private UserEntity user;

  @Column(nullable = false)
  private Long noticeId;

  @Column(nullable = false)
  private boolean ignored;

  public static NoticeUserEntity of(Long userId, Long noticeId, boolean ignored) {
    UserEntity user = UserEntity.builder().id(userId).build();
    return NoticeUserEntity.builder()
        .user(user)
        .noticeId(noticeId)
        .ignored(ignored)
        .build();
  }

  public static NoticeUserEntity updateIgnored(NoticeUserEntity entity) {
    return NoticeUserEntity.builder()
        .id(entity.getId())
        .user(entity.getUser())
        .noticeId(entity.getNoticeId())
        .ignored(true)
        .build();
  }

}
