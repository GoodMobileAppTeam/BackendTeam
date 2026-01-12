package mobile.backend.videoEdit.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import mobile.backend.global.config.S3.S3Properties;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmItemResponse;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmListResponse;
import mobile.backend.videoEdit.application.port.in.BgmQueryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BgmService implements BgmQueryUseCase {

  // 음악 파일 정보
  private record FileMeta(String title, String artist) {}

  private final AmazonS3Manager amazonS3Manager;
  private final S3Properties s3Properties;

  @Override
  public BgmListResponse getBgmList() {

    // 실제 S3 구조 기준 : bgm/
    String prefix = s3Properties.getPath().getBgm() + "/";

    List<String> keys = amazonS3Manager.listKeys(prefix);

    // thumbnail 폴더의 모든 파일 목록 가져오기
    String thumbnailPrefix = s3Properties.getPath().getThumbnail() + "/"; //  bgm/thumbnail/
    List<String> thumbnailKeys = amazonS3Manager.listKeys(thumbnailPrefix);

    List<BgmItemResponse> bgms = new ArrayList<>();

    for (String key : keys) {
      if (!key.toLowerCase().endsWith(".mp3")) continue;

      FileMeta meta = parseFileName(key);
      String thumbnailUrl = getThumbnailUrl(meta.title, thumbnailKeys);

      bgms.add(
          BgmItemResponse.builder()
              .title(meta.title)
              .artist(meta.artist)
              .thumbnailUrl(thumbnailUrl)
              .audioUrl(amazonS3Manager.getPublicUrl(key))
              .build()
      );
    }

    return BgmListResponse.from(bgms);
  }

  /**
   * <pre>
   * BGM 제목에 대응하는 썸네일 URL을 반환
   *
   * 썸네일 파일 규칙:
   * - BGM 파일명: "Scandinavianz - Chill Guy.mp3"
   * - BGM title: "Chill Guy"
   * - 썸네일 파일명: "Chill Guy.jpg" (title과 동일, .jpg/.jpeg)
   *
   * 예시:
   * - title: "Chill Guy" → 썸네일: "Chill Guy.jpg"
   * - title: "Bicycles" → 썸네일: "Bicycles.jpg"
   * - title: "hope" → 썸네일: "hope.jpg"
   *
   * 썸네일이 없으면 null 반환
   * </pre>
   */
  private String getThumbnailUrl(String title, List<String> thumbnailKeys) {
    for (String thumbnailKey : thumbnailKeys) {
      // "bgm/thumbnail/Chill Guy.jpg" → "Chill Guy.jpg"
      String thumbnailFileName = thumbnailKey.substring(thumbnailKey.lastIndexOf("/") + 1);
      
      // 확장자 제거: "Chill Guy.jpg" → "Chill Guy"
      String thumbnailNameWithoutExt = thumbnailFileName.replaceAll("(?i)\\.(jpg|jpeg)$", "");
      
      // 대소문자 구분 없이 비교
      if (thumbnailNameWithoutExt.equalsIgnoreCase(title)) {
        return amazonS3Manager.getPublicUrl(thumbnailKey);
      }
    }
    
    return null; // 썸네일이 없으면 null
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
   * 1) "Scandinavianz - Chill Guy.mp3"
   *    → artist = "Scandinavianz"
   *    → title  = "Chill Guy"
   *
   * 2) "Tokiwave - jellyfish.mp3"
   *    → artist = "Tokiwave"
   *    → title  = "jellyfish"
   *
   * 3) "hope.mp3"
   *    → artist = "Unknown"
   *    → title  = "hope"
   *
   * 4) "MaxKoMusic - Happy commercial.mp3"
   *    → artist = "MaxKoMusic"
   *    → title  = "Happy commercial"
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
          parts[1].trim(),  // title
          parts[0].trim()   // artist
      );
    }

    // 아티스트 정보가 없는 경우
    return new FileMeta(
        fileName.trim(),   // title
        "Unknown"          // artist
    );
  }
}
