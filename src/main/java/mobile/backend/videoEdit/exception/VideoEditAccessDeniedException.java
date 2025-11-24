package mobile.backend.videoEdit.exception;

public class VideoEditAccessDeniedException extends RuntimeException {

    public VideoEditAccessDeniedException(Long videoEditId, Long userId) {
        super(String.format("해당 영상에 대한 접근 권한이 없습니다. VideoEditId: %d, UserId: %d",
                videoEditId, userId));
    }
}
