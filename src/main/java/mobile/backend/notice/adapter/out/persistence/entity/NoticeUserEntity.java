package mobile.backend.notice.adapter.out.persistence.entity;

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

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long noticeId;

  @Column(nullable = false)
  private boolean ignored;

  public static NoticeUserEntity of(Long userId, Long noticeId, boolean ignored) {
    return NoticeUserEntity.builder()
        .userId(userId)
        .noticeId(noticeId)
        .ignored(ignored)
        .build();
  }

  public static NoticeUserEntity updateIgnored(NoticeUserEntity entity) {
    return NoticeUserEntity.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .noticeId(entity.getNoticeId())
        .ignored(true)
        .build();
  }

}
