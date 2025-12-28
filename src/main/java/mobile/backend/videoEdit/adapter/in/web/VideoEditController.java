package mobile.backend.videoEdit.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.global.security.CustomUserDetails;
import mobile.backend.videoEdit.adapter.in.web.request.CreateVideoEditRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditBookmarkSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.request.VideoEditSummaryRequest;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditSearchResponse;
import mobile.backend.videoEdit.adapter.in.web.request.bgm.SetVideoEditBgmRequest;
import mobile.backend.videoEdit.adapter.in.web.request.place.PlaceNameSearchRequest;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditBookmarkSearchResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditListResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditResponse;
import mobile.backend.videoEdit.adapter.in.web.response.VideoEditSummaryResponse;
import mobile.backend.videoEdit.application.port.in.*;
import mobile.backend.videoEdit.application.service.CursorPageResult;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameResponse;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmListResponse;
import mobile.backend.videoEdit.adapter.in.web.response.place.PlaceNameSearchResponse;
import mobile.backend.videoEdit.application.port.in.BgmCommandUseCase;
import mobile.backend.videoEdit.application.port.in.BgmQueryUseCase;
import mobile.backend.videoEdit.application.port.in.PlaceNameQueryUseCase;
import mobile.backend.videoEdit.application.port.in.VideoEditCommandUseCase;
import mobile.backend.videoEdit.application.port.in.VideoEditQueryUseCase;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
  private final PlaceNameQueryUseCase placeNameQueryUseCase;

    @Operation(
            summary = "영상 등록",
            description = "새로운 영상을 등록합니다. 영상 정보(JSON)와 썸네일 이미지를 함께 전송해야 합니다. " +
                    "영상 길이는 최대 60초까지 가능합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<VideoEditResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestPart("request") CreateVideoEditRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {

        CreateVideoEditCommand command = request.toCommand(userDetails.getUserId(), thumbnail);

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
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        VideoEdit videoEdit = videoEditQueryUseCase.getById(id, userDetails.getUserId());
        return ResponseEntity.ok(BaseResponse.success(VideoEditResponse.from(videoEdit)));
    }

    @Operation(
            summary = "보드 영상 목록 조회",
            description = "사용자의 영상 목록을 커서 페이징하여 조회합니다."
    )
    @GetMapping
    public ResponseEntity<BaseResponse<VideoEditSearchResponse>> search(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VideoEditSearchRequest request) {

        SearchVideoEditCommand command = request.toCommand(userDetails.getUserId(), false);

        CursorPageResult<VideoEdit> pageResult = videoEditQueryUseCase.search(command);

        return ResponseEntity.ok(BaseResponse.success(VideoEditSearchResponse.from(pageResult)));
    }

    @Operation(
            summary = "북마크한 영상 목록 조회",
            description = "사용자가 북마크한 영상 목록을 커서 페이징하여 조회합니다."
    )
    @GetMapping("/bookmarks")
    public ResponseEntity<BaseResponse<VideoEditSearchResponse>> getBookmarked(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VideoEditSearchRequest request) {

        SearchVideoEditCommand command = request.toCommand(userDetails.getUserId(), true);

        CursorPageResult<VideoEdit> pageResult = videoEditQueryUseCase.search(command);

        return ResponseEntity.ok(BaseResponse.success(VideoEditSearchResponse.from(pageResult)));
    }

    @Operation(
            summary = "북마크 토글",
            description = "영상의 북마크 상태를 변경합니다. 북마크되어 있으면 해제하고, 해제되어 있으면 추가합니다."
    )
    @PatchMapping("/{id}/bookmark")
    public ResponseEntity<BaseResponse<VideoEditResponse>> toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        VideoEdit updated = videoEditCommandUseCase.toggle(id, userDetails.getUserId());
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
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        videoEditCommandUseCase.delete(id, customUserDetails.getUserId ());
        return ResponseEntity.ok(BaseResponse.success("영상이 삭제되었습니다.", null));
    }

    @Operation(
            summary = "영상 정보 요약 반환",
            description = "날짜 범위에 맞는 목록의 요약을 반환 (yyyy-MM-dd 형식 사용)"
    )
    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<List<VideoEditSummaryResponse>>> getDailySummary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VideoEditSummaryRequest request) {

        SearchSummaryVideoEditCommand command = request.toCommand(userDetails.getUserId());

        List<VideoEditSummary> summaries = videoEditQueryUseCase.getDailySummary(command);

        return ResponseEntity.ok(BaseResponse.success(VideoEditSummaryResponse.fromDomainList(summaries)));
  }

  @Operation(
      summary = "키워드 기반 장소 검색",
      description = """
          입력한 키워드(query)로 키워드 장소 검색을 수행합니다.<br>
          응답은 placeName + address(도로명 우선, 없으면 지번) 형태로 반환합니다.<br>
          
          페이징: <br>
           - page: 1 ~ 45
           - size: 1 ~ 15
          """
  )
  @GetMapping("/placeNames/search")
  public BaseResponse<PlaceNameSearchResponse> getPlaceNames(
      @Valid @ModelAttribute PlaceNameSearchRequest request
  ) {
    return BaseResponse.success(placeNameQueryUseCase.search(request.toCommand()));
  }

  @Operation(
      summary = "배경음악 목록 조회",
      description = "S3 bgm/ 폴더의 mp3 목록을 조회하여 반환합니다."
  )
  @GetMapping("/bgms")
  public ResponseEntity<BaseResponse<BgmListResponse>> getBgms() {
    return ResponseEntity.ok(BaseResponse.success(bgmQueryUseCase.getBgmList()));
  }

  @Operation(
      summary = "배경음악 설정",
      description = "영상에 bgmKey(S3에 저장된 배경음악 파일의 경로(Key)) 를 저장합니다."
  )
  @PatchMapping("/{id}/bgm")
  public ResponseEntity<BaseResponse<Void>> setBgm(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody SetVideoEditBgmRequest request
  ) {
    bgmCommandUseCase.setBgm(request.toCommand(id, userDetails.getUserId()));
    return ResponseEntity.ok(BaseResponse.success("배경음악이 설정되었습니다.", null));
  }

  @Operation(
      summary = "배경음악 해제",
      description = "영상에 설정된 bgmKey를 제거합니다."
  )
  @DeleteMapping("/{id}/bgm")
  public ResponseEntity<BaseResponse<Void>> clearBgm(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    bgmCommandUseCase.clearBgm(id, userDetails.getUserId());
    return ResponseEntity.ok(BaseResponse.success("배경음악이 해제되었습니다.", null));
  }


}
