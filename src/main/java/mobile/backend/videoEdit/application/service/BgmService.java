package mobile.backend.videoEdit.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import mobile.backend.global.config.S3.S3Properties;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmItemResponse;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmListResponse;
import mobile.backend.videoEdit.application.port.in.BgmCommandUseCase;
import mobile.backend.videoEdit.application.port.in.BgmQueryUseCase;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.bgm.SetVideoEditBgmCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BgmService implements BgmCommandUseCase, BgmQueryUseCase {

  // 음악 파일 정보
  private record FileMeta(String title, String artist) {}

  private final VideoEditRepository videoEditRepository;

  private final AmazonS3Manager amazonS3Manager;
  private final S3Properties s3Properties;

  @Override
  @Transactional
  public void setBgm(SetVideoEditBgmCommand command) {
    VideoEdit videoEdit = videoEditRepository.findById(command.getVideoId());

    VideoEdit.validateOwnership(videoEdit, command.getUserId());

    videoEdit.setBgm(command.getBgmKey());

    videoEditRepository.save(videoEdit);
  }

  @Override
  @Transactional
  public void clearBgm(Long videoId, Long userId) {
    VideoEdit videoEdit = videoEditRepository.findById(videoId);

    VideoEdit.validateOwnership(videoEdit, userId);

    videoEdit.clearBgm();

    videoEditRepository.save(videoEdit);

  }

  @Override
  public BgmListResponse getBgmList() {

    // 실제 S3 구조 기준 : bgm/
    String prefix = s3Properties.getPath().getBgm() + "/";

    List<String> keys = amazonS3Manager.listKeys(prefix);

    List<BgmItemResponse> bgms = new ArrayList<>();

    for (String key : keys) {
      if (!key.toLowerCase().endsWith(".mp3")) continue;

      FileMeta meta = parseFileName(key);

      bgms.add(
          BgmItemResponse.builder()
              .bgmKey(key) // bgm/hope.mp3
              .title(meta.title)
              .artist(meta.artist)
              .thumbnailUrl(null) // 지금은 없음
              .audioUrl(amazonS3Manager.getPublicUrl(key))
              .build()
      );
    }

    return BgmListResponse.from(bgms);
  }

  /**
   * <pre>
   * 파일명 기반으로 음악 정보 파싱
   *
   * 파싱 규칙
   *
   * 1. 확장자(.mp3, .MP3) 제거
   *    - 대소문자 구분 없이 제거
   *
   * 2. " - " (공백-하이픈-공백)이 있으면
   *    → "아티스트 - 제목" 형식으로 판단
   *
   * 3. " - "이 없으면
   *    → 아티스트 정보 없음으로 간주
   *
   * 예시
   *
   * 1) "Scandinavianz - Chill Guy mp3.mp3"
   *    → artist = "Scandinavianz"
   *    → title  = "Chill Guy mp3"
   *
   * 2) "Tokiwae - jellyfish (mp3).mp3"
   *    → artist = "Tokiwae"
   *    → title  = "jellyfish (mp3)"
   *
   * 3) "hope.mp3"
   *    → artist = "Unknown"
   *    → title  = "hope"
   *
   * 4) "Bubble.MP3"
   *    → artist = "Unknown"
   *    → title  = "Bubble"
   *</pre>
   */
  private FileMeta parseFileName(String key) {

    // "bgm/파일명.mp3" → "파일명.mp3"
    String fileName = key.substring(key.lastIndexOf("/") + 1);

    // 확장자 제거 (.mp3, .MP3)
    fileName = fileName.replaceAll("(?i)\\.mp3$", "");

    // "아티스트 - 제목" 형식인 경우
    if (fileName.contains(" - ")) {
      String[] parts = fileName.split(" - ", 2);
      return new FileMeta(
          parts[0].trim(), // artist
          parts[1].trim()  // title
      );
    }

    // 아티스트 정보가 없는 경우
    return new FileMeta(
        "Unknown",         // artist
        fileName.trim()    // title
    );
  }
}
