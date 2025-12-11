package mobile.backend.videoEdit.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.videoEdit.adapter.in.web.request.CreateVideoEditRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditBookmarkSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditSummaryRequest;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditBookmarkSearchResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditListResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditSummaryResponse;
import mobile.backend.videoEdit.application.port.in.*;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Video Edit", description = "영상 편집 관리 API")
@RestController
@RequestMapping("/v1/videos")
@RequiredArgsConstructor
public class VideoEditController {

    private final VideoEditCommandUseCase videoEditCommandUseCase;
    private final VideoEditQueryUseCase videoEditQueryUseCase;

    @Operation(
            summary = "영상 등록",
            description = "새로운 영상을 등록합니다. 영상 정보(JSON)와 썸네일 이미지를 함께 전송해야 합니다. " +
                    "영상 길이는 최대 60초까지 가능합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<VideoEditResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestPart("request") CreateVideoEditRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {

        CreateVideoEditCommand command = request.toCommand(userDetails.getUsername(), thumbnail);

        VideoEdit created = videoEditCommandUseCase.create(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("영상이 등록되었습니다.", VideoEditResponse.from(created)));
    }

    @Operation(
            summary = "영상 단건 조회",
            description = "영상 ID로 특정 영상의 상세 정보를 조회합니다. 본인이 등록한 영상만 조회 가능합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<VideoEditResponse>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        VideoEdit videoEdit = videoEditQueryUseCase.getById(id, userDetails.getUsername());
        return ResponseEntity.ok(BaseResponse.success(VideoEditResponse.from(videoEdit)));
    }

    @Operation(
            summary = "영상 목록 조회",
            description = "날짜 범위에 맞는 목록을 반환 (yyyy-MM-dd 형식 사용, 커서 페이징 사용)"
    )
    @GetMapping
    public ResponseEntity<BaseResponse<VideoEditListResponse>> search(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute VideoEditSearchRequest request) {

        SearchVideoEditCommand criteria = request.toCommand(userDetails.getUsername());

        List<VideoEdit> result = videoEditQueryUseCase.search(criteria);
        return ResponseEntity.ok(BaseResponse.success(VideoEditListResponse.from(result, request.size())));
    }

    @Operation(
            summary = "북마크한 영상 목록 조회",
            description = "사용자가 북마크한 영상 목록을 커서 페이징하여 조회합니다."
    )
    @GetMapping("/bookmarks")
    public ResponseEntity<BaseResponse<VideoEditBookmarkSearchResponse>> getBookmarked(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute VideoEditBookmarkSearchRequest request) {

        SearchBookmarkVideoEditCommand command = request.toCommand(userDetails.getUsername());

        List<VideoEdit> result = videoEditQueryUseCase.getBookmarkedVideos(command);

        return ResponseEntity.ok(BaseResponse.success(VideoEditBookmarkSearchResponse.from(result, request.size()))
        );
    }

    @Operation(
            summary = "북마크 토글",
            description = "영상의 북마크 상태를 변경합니다. 북마크되어 있으면 해제하고, 해제되어 있으면 추가합니다."
    )
    @PatchMapping("/{id}/bookmark")
    public ResponseEntity<BaseResponse<VideoEditResponse>> toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        VideoEdit updated = videoEditCommandUseCase.toggle(id, userDetails.getUsername());
        String message = updated.isBookMarked() ? "북마크가 추가되었습니다." : "북마크가 해제되었습니다.";
        return ResponseEntity.ok(BaseResponse.success(message, VideoEditResponse.from(updated)));
    }

    @Operation(
            summary = "영상 삭제",
            description = "영상을 삭제합니다. 영상과 함께 S3에 저장된 썸네일도 함께 삭제됩니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        videoEditCommandUseCase.delete(id, userDetails.getUsername());
        return ResponseEntity.ok(BaseResponse.success("영상이 삭제되었습니다.", null));
    }

    @Operation(
            summary = "영상 정보 요약 반환",
            description = "날짜 범위에 맞는 목록의 요약을 반환 (yyyy-MM-dd 형식 사용)"
    )
    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<List<VideoEditSummaryResponse>>> getDailySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute VideoEditSummaryRequest request) {

        SearchSummaryVideoEditCommand command = request.toCommand(userDetails.getUsername());

        List<VideoEditSummary> summaries = videoEditQueryUseCase.getDailySummary(command);

        return ResponseEntity.ok(BaseResponse.success(VideoEditSummaryResponse.fromDomainList(summaries)));
    }
}
