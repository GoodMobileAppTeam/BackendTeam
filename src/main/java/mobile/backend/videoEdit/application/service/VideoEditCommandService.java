package mobile.backend.videoEdit.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.application.port.in.VideoEditCommandUseCase;
import mobile.backend.videoEdit.application.port.in.VideoEditQueryUseCase;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoEditCommandService implements
        VideoEditCommandUseCase,
        VideoEditQueryUseCase {

    private final VideoEditRepository videoEditRepository;
    private final AmazonS3Manager s3Manager;

    @Override
    @Transactional
    public VideoEdit create(CreateVideoEditCommand command) {


        String thumbnailUrl = s3Manager.uploadFile(
                command.thumbnailFileName(),
                command.thumbnailData()
        );

        VideoEdit videoEdit = VideoEdit.create(
                command.albumId(),
                command.userId(),
                command.duration(),
                command.videoUrl(),
                command.saveTime(),
                thumbnailUrl,
                command.description()
        );

        return videoEditRepository.save(videoEdit);
    }

    @Override
    public VideoEdit getById(Long id, Long userId) {
        VideoEdit videoEdit = findVideoEditOrThrow(id);
        validateOwnership(videoEdit, userId);
        return videoEdit;
    }

    @Override
    public Page<VideoEdit> search(SearchVideoEditCommand criteria) {
        return videoEditRepository.search(criteria);
    }

    @Override
    public Page<VideoEdit> getBookmarkedVideos(Long userId, int page, int size) {
        SearchVideoEditCommand criteria = SearchVideoEditCommand.bookmarked(userId, page, size);
        return videoEditRepository.search(criteria);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        VideoEdit videoEdit = findVideoEditOrThrow(id);
        validateOwnership(videoEdit, userId);

        if (videoEdit.getThumbnailUrl() != null) {
            s3Manager.deleteObjectByUrl(videoEdit.getThumbnailUrl());
        }

        videoEditRepository.delete(videoEdit);
    }

    @Override
    @Transactional
    public VideoEdit toggle(Long videoEditId, Long userId) {
        VideoEdit videoEdit = findVideoEditOrThrow(videoEditId);
        validateOwnership(videoEdit, userId);

        videoEdit.toggleBookmark();
        return videoEditRepository.save(videoEdit);
    }

    private VideoEdit findVideoEditOrThrow(Long id) {
        return videoEditRepository.findById(id)
                .orElseThrow(() -> new CustomException(VideoErrorCode.VIDEO_NOT_FOUND));
    }

    private void validateOwnership(VideoEdit videoEdit, Long userId) {
        if (!videoEdit.isOwnedBy(userId)) {
            throw new CustomException(VideoErrorCode.VIDEO_ACCESS_DENIED);
        }
    }
}
