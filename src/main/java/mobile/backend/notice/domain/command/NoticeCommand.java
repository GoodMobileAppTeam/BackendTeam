package mobile.backend.notice.domain.command;

import lombok.Getter;

@Getter
public class NoticeCommand {
  private final String title;
  private final String content;

  public NoticeCommand(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public static NoticeCommand of(String title, String content) {
    return new NoticeCommand(title, content);
  }
}
