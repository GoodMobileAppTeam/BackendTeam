package mobile.backend.notice.domain.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Notice {
  private final Long id;
  private final String title;
  private final String content;
  private final LocalDateTime createdAt;

  public Notice(Long id, String title, String content, LocalDateTime createdAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
  }

  public static Notice of(Long id, String title, String content, LocalDateTime createdAt) {
    return new Notice(id, title, content, createdAt);
  }

  public static Notice create(String title, String content) {
    return new Notice(
        null, // DB에 저장할때 id 채워짐
        title,
        content,
        LocalDateTime.now()
    );
  }
}
