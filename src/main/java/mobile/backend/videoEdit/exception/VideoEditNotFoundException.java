package mobile.backend.videoEdit.exception;

public class VideoEditNotFoundException extends RuntimeException {

    public VideoEditNotFoundException(Long id) {
        super(String.format("영상 편집 정보를 찾을 수 없습니다. ID: %d", id));
    }
}
