package mobile.backend.videoEdit.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import mobile.backend.global.config.S3.S3Properties;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmItemResponse;
import mobile.backend.videoEdit.adapter.in.web.response.bgm.BgmListResponse;
import mobile.backend.videoEdit.application.port.in.BgmQueryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BgmService implements BgmQueryUseCase {

  // 음악 파일 정보
  private record FileMeta(String title, String artist) {}

  /**
   * 곡별 태그 매핑 (key: 곡명 소문자)
   *
   * Map.ofEntries() : Java 9+ 불변(immutable) Map 생성 메서드. 10개 초과 엔트리도 가능.
   *                   (10개 이하는 Map.of(k1,v1, k2,v2, ...) 도 사용 가능)
   * Map.entry(K, V) : Map.ofEntries()에 넣을 key-value 쌍 하나를 생성
   * List.of(...)     : Java 9+ 불변(immutable) List 생성
   *
   * 생성된 Map과 List 모두 수정 불가(put/add 시 UnsupportedOperationException 발생)
   */
  private static final Map<String, List<String>> TAG_MAP = Map.ofEntries(
      Map.entry("bicycles", List.of("발랄한", "귀여운", "활발한")),
      Map.entry("cappuccino", List.of("로맨틱", "사랑스러운", "아늑한")),
      Map.entry("chill guy", List.of("힙한", "다이나믹한", "신나는")),
      Map.entry("jellyfish", List.of("발랄한", "귀여운", "활발한")),
      Map.entry("hope", List.of("웅장한", "활기찬", "모험적")),
      Map.entry("bubble", List.of("발랄한", "귀여운", "활발한")),
      Map.entry("wistful thinking", List.of("감미로운", "감성적인", "슬픈")),
      Map.entry("smile", List.of("즐거운", "행복한", "활발한")),
      Map.entry("ukulele and piano", List.of("발랄한", "귀여운", "활기찬")),
      Map.entry("happy commercial", List.of("발랄한", "귀여운", "활기찬"))
  );

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

      // getOrDefault: TAG_MAP에 해당 곡명이 있으면 태그 리스트 반환, 없으면 두 번째 인자 반환
      // Collections.emptyList(): null 대신 빈 리스트 [] 를 반환 → 프론트에서 "tags": [] 로 응답되어 null 체크 불필요
      List<String> tags = TAG_MAP.getOrDefault(
          meta.title.toLowerCase(), Collections.emptyList());

      bgms.add(
          BgmItemResponse.builder()
              .title(meta.title)
              .artist(meta.artist)
              .thumbnailUrl(thumbnailUrl)
              .audioUrl(amazonS3Manager.getPublicUrl(key))
              .tags(tags)
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
