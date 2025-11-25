package mobile.backend.notice.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import mobile.backend.notice.domain.command.NoticeCommand;

@Getter
@Schema(title = "NoticeRequest : 공지 요청 DTO")
@Setter //@ModelAttribute 바인딩할 때는 setter 메서드를 사용해 값을 채워 넣음
public class NoticeRequest {

  @NotBlank(message = "제목은 필수입니다.")
  @Schema(example = "서버 점검 알림")
  private String title;

  @NotBlank(message = "내용은 필수입니다.")
  @Schema(example = "2025년 12월 31일 00:00 ~ 06:00(6시간) 서버 점검 예정")
  private String content;

  public NoticeCommand toCommand() {
    return NoticeCommand.of(this.title, this.content);
  }

}
