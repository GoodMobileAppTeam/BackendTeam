package mobile.backend.videoEdit.application.service;

import lombok.RequiredArgsConstructor;
import mobile.backend.videoEdit.application.port.in.CreateVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.DeleteVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.GetVideoEditUseCase;
import mobile.backend.videoEdit.application.port.in.ToggleBookmarkUseCase;
import mobile.backend.videoEdit.application.port.in.UpdateVideoEditUseCase;
import mobile.backend.videoEdit.application.port.out.FileStoragePort;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.UpdateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.VideoEditSearchCriteria;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.exception.VideoEditAccessDeniedException;
import mobile.backend.videoEdit.exception.VideoEditNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoEditService implements
        CreateVideoEditUseCase,
        GetVideoEditUseCase,
        UpdateVideoEditUseCase,
        DeleteVideoEditUseCase,
        ToggleBookmarkUseCase {

    private final VideoEditRepository videoEditRepository;
    private final FileStoragePort fileStoragePort;

    private static final String THUMBNAIL_DIRECTORY = "thumbnails";

    @Override
    @Transactional
    public VideoEdit create(CreateVideoEditCommand command) {
        String thumbnailUrl = fileStoragePort.upload(
                command.thumbnailData(),
                command.thumbnailFileName(),
                THUMBNAIL_DIRECTORY
        );

        VideoEdit videoEdit = VideoEdit.create(
                command.albumId(),
                command.userId(),
                command.duration(),
                command.videoUrl(),
                command.saveTime(),
                thumbnailUrl
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
    public Page<VideoEdit> search(VideoEditSearchCriteria criteria) {
        return videoEditRepository.search(criteria);
    }

    @Override
    public Page<VideoEdit> getBookmarkedVideos(Long userId, int page, int size) {
        VideoEditSearchCriteria criteria = VideoEditSearchCriteria.bookmarked(userId, page, size);
        return videoEditRepository.search(criteria);
    }

    @Override
    @Transactional
    public VideoEdit updateThumbnail(UpdateVideoEditCommand command) {
        VideoEdit videoEdit = findVideoEditOrThrow(command.videoEditId());
        validateOwnership(videoEdit, command.userId());

        if (command.thumbnailData() != null && command.thumbnailData().length > 0) {
            if (videoEdit.getThumbnailUrl() != null) {
                fileStoragePort.delete(videoEdit.getThumbnailUrl());
            }

            String newThumbnailUrl = fileStoragePort.upload(
                    command.thumbnailData(),
                    command.thumbnailFileName(),
                    THUMBNAIL_DIRECTORY
            );
            videoEdit.updateThumbnailUrl(newThumbnailUrl);
        }

        return videoEditRepository.save(videoEdit);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        VideoEdit videoEdit = findVideoEditOrThrow(id);
        validateOwnership(videoEdit, userId);

        if (videoEdit.getThumbnailUrl() != null) {
            fileStoragePort.delete(videoEdit.getThumbnailUrl());
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
                .orElseThrow(() -> new VideoEditNotFoundException(id));
    }

    private void validateOwnership(VideoEdit videoEdit, Long userId) {
        if (!videoEdit.isOwnedBy(userId)) {
            throw new VideoEditAccessDeniedException(videoEdit.getId(), userId);
        }
    }
}
