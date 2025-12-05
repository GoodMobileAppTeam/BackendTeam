package mobile.backend.user.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.user.domain.model.SocialType;
import mobile.backend.user.domain.model.User;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_social",
            columnNames = {"social_id", "social_type"}
        )
    }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "social_id", nullable = false)
  private String socialId;

  @Enumerated(EnumType.STRING)
  @Column(name = "social_type", nullable = false)
  private SocialType socialType;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public static UserEntity from(User user) {
    return UserEntity.builder()
            .name(user.getName())
            .socialId(user.getSocialId())
            .socialType(user.getSocialType())
            .profileImageUrl(user.getProfileImageUrl())
            .createdAt(user.getCreatedAt())
            .build();
  }

  public User toDomain() {
    return User.of(
        this.id,
        this.name,
        this.socialId,
        this.socialType,
        this.profileImageUrl,
        this.createdAt
    );
  }
}
