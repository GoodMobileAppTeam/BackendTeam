package mobile.backend.videoEdit.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import mobile.backend.videoEdit.application.service.querymodel.CursorPageResult;
import mobile.backend.videoEdit.domain.model.VideoEdit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
        title = "VideoEditSearchResponse : 영상 목록 응답 DTO",
        description = """
        영상 목록 조회 응답 DTO입니다.

        - 조회 결과가 존재하는 경우: 영상 목록과 커서 정보가 함께 반환됩니다.
        - 조회 결과가 없는 경우: content는 빈 배열([])이며, 모든 커서 정보는 null,
          hasNext / hasPrev 는 false로 반환됩니다.

        예시 (데이터가 없는 경우):
        {
          "success": true,
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": []
        }
        """
)
public record VideoEditSearchResponse(

        @Schema(description = "영상 목록 (null able)", nullable = true)
        List<VideoEditResponse> content,

        // NEXT
        @Schema(description = "다음 페이지 커서 - 사용자 지정 날짜 (null able)", nullable = true)
        LocalDate nextCursorUserDefinedDate,

        @Schema(description = "다음 페이지 커서 - 생성 시각 (null able)", nullable = true)
        LocalDateTime nextCursorCreatedAt,

        @Schema(description = "다음 페이지 커서 - ID (null able)", nullable = true)
        Long nextCursorId,

        @Schema(description = "다음 페이지 존재 여부", nullable = true)
        boolean hasNext,

        // PREV
        @Schema(description = "이전 페이지 커서 - 사용자 지정 날짜 (null able)", nullable = true)
        LocalDate prevCursorUserDefinedDate,

        @Schema(description = "이전 페이지 커서 - 생성 시각 (null able)", nullable = true)
        LocalDateTime prevCursorCreatedAt,

        @Schema(description = "이전 페이지 커서 - ID (null able)", nullable = true)
        Long prevCursorId,

        @Schema(description = "이전 페이지 존재 여부", nullable = true)
        boolean hasPrev
)  {

    public static VideoEditSearchResponse from(CursorPageResult<VideoEdit> page) {

        if (page.content().isEmpty()) {
            return new VideoEditSearchResponse(
                    List.of(),
                    null, null, null, false,
                    null, null, null, false
            );
        }

        VideoEdit first = page.content().get(0);
        VideoEdit last  = page.content().get(page.content().size() - 1);

        return new VideoEditSearchResponse(
                page.content().stream()
                        .map(VideoEditResponse::from)
                        .toList(),

                page.hasNext() ? last.getUserDefinedDate() : null,
                page.hasNext() ? last.getCreatedAt() : null,
                page.hasNext() ? last.getId() : null,
                page.hasNext(),

                page.hasPrev() ? first.getUserDefinedDate() : null,
                page.hasPrev() ? first.getCreatedAt() : null,
                page.hasPrev() ? first.getId() : null,
                page.hasPrev()
        );
    }
}
