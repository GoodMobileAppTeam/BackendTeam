package mobile.backend.user.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mobile.backend.user.domain.model.TestUser;

@Entity
@Table(name = "test_users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static TestUserEntity from(TestUser testUser) {
        return TestUserEntity.builder()
                .name(testUser.getName())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .createdAt(testUser.getCreatedAt())
                .build();
    }

    public TestUser toDomain() {
        return TestUser.of(this.id, this.name, this.email, this.password, this.createdAt);
    }
}
