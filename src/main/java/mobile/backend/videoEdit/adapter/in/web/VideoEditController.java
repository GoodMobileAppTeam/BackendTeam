package mobile.backend.videoEdit.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.videoEdit.adapter.in.web.request.CreateVideoEditRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditPageResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditResponse;
import mobile.backend.videoEdit.application.port.in.CreateVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.DeleteVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.GetVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.ToggleBookmarkUseCase;
import mobile.backend.videoEdit.application.port.in.UpdateVideoEditUseCase;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.UpdateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.VideoEditSearchCriteria;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/videos")
@RequiredArgsConstructor
public class VideoEditController {

    private final CreateVideoEditUseCase createVideoEditUseCase;
    private final GetVideoEditUseCase getVideoEditUseCase;
    private final UpdateVideoEditUseCase updateVideoEditUseCase;
    private final DeleteVideoEditUseCase deleteVideoEditUseCase;
    private final ToggleBookmarkUseCase toggleBookmarkUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<VideoEditResponse>> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestPart("request") CreateVideoEditRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {

        CreateVideoEditCommand command = new CreateVideoEditCommand(
                request.albumId(),
                userId,
                request.duration(),
                request.videoUrl(),
                thumbnail.getBytes(),
                thumbnail.getOriginalFilename(),
                request.saveTime(),
                request.description()
        );

        VideoEdit created = createVideoEditUseCase.create(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("영상이 등록되었습니다.", VideoEditResponse.from(created)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<VideoEditResponse>> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        VideoEdit videoEdit = getVideoEditUseCase.getById(id, userId);
        return ResponseEntity.ok(BaseResponse.success(VideoEditResponse.from(videoEdit)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<VideoEditPageResponse>> search(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @ModelAttribute VideoEditSearchRequest request) {

        VideoEditSearchCriteria criteria = VideoEditSearchCriteria.of(
                userId,
                request.year(),
                request.month(),
                request.isBookMarked(),
                request.page(),
                request.size()
        );

        Page<VideoEdit> result = getVideoEditUseCase.search(criteria);
        return ResponseEntity.ok(BaseResponse.success(VideoEditPageResponse.from(result)));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<BaseResponse<VideoEditPageResponse>> getBookmarked(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<VideoEdit> result = getVideoEditUseCase.getBookmarkedVideos(userId, page, size);
        return ResponseEntity.ok(BaseResponse.success(VideoEditPageResponse.from(result)));
    }

    @PatchMapping(value = "/{id}/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<VideoEditResponse>> updateThumbnail(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {

        UpdateVideoEditCommand command = new UpdateVideoEditCommand(
                id,
                userId,
                thumbnail.getBytes(),
                thumbnail.getOriginalFilename()
        );

        VideoEdit updated = updateVideoEditUseCase.updateThumbnail(command);
        return ResponseEntity.ok(
                BaseResponse.success("썸네일이 수정되었습니다.", VideoEditResponse.from(updated))
        );
    }

    @PatchMapping("/{id}/bookmark")
    public ResponseEntity<BaseResponse<VideoEditResponse>> toggleBookmark(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        VideoEdit updated = toggleBookmarkUseCase.toggle(id, userId);
        String message = updated.isBookMarked() ? "북마크가 추가되었습니다." : "북마크가 해제되었습니다.";
        return ResponseEntity.ok(BaseResponse.success(message, VideoEditResponse.from(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        deleteVideoEditUseCase.delete(id, userId);
        return ResponseEntity.ok(BaseResponse.success("영상이 삭제되었습니다.", null));
    }
}
