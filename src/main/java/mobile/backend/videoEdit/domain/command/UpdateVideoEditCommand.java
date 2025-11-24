package mobile.backend.videoEdit.domain.command;

public record UpdateVideoEditCommand(
        Long videoEditId,
        Long userId,
        byte[] thumbnailData,
        String thumbnailFileName
) {
    public UpdateVideoEditCommand {
        if (videoEditId == null) throw new IllegalArgumentException("videoEditId는 필수입니다.");
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
    }
}
