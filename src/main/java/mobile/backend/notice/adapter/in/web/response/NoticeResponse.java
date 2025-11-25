package mobile.backend.notice.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import mobile.backend.notice.domain.model.Notice;

@Getter
@Builder
@Schema(title = "NoticeResponse : 공지 응답 DTO")
public class NoticeResponse {

  @Schema(description = "공지 id", example = "1")
  private Long id;

  @Schema(description = "공지 제목", example = "서버 점검 알림")
  private String title;

  @Schema(description = "공지 내용", example = "2026년 1월 20일 00:00 ~ 06:00(6시간) 서버 점검 예정")
  private String content;

  @Schema(description = "공지 올린 시각", example = "2025-11-25")
  private LocalDateTime createdAt;

  public static NoticeResponse from(Notice notice) {
    return NoticeResponse.builder()
        .id(notice.getId())
        .title(notice.getTitle())
        .content(notice.getContent())
        .createdAt(notice.getCreatedAt())
        .build();
  }
}
