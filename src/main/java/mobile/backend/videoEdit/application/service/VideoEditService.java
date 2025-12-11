package mobile.backend.videoEdit.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.global.adapter.out.s3.AmazonS3Manager;
import mobile.backend.videoEdit.application.port.in.VideoEditCommandUseCase;
import mobile.backend.videoEdit.application.port.in.VideoEditQueryUseCase;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.CreateVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoEditService implements VideoEditCommandUseCase, VideoEditQueryUseCase {

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
        VideoEdit videoEdit = videoEditRepository.findById(id);
        VideoEdit.validateOwnership(videoEdit, userId);
        return videoEdit;
    }

    @Override
    public List<VideoEdit> search(SearchVideoEditCommand criteria) {
        return videoEditRepository.search(criteria);
    }

    @Override
    public List<VideoEdit> getBookmarkedVideos(SearchBookmarkVideoEditCommand command) {
        return videoEditRepository.bookmarkSearch(command);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        VideoEdit videoEdit = videoEditRepository.findById(id);
        VideoEdit.validateOwnership(videoEdit, userId);

        if (videoEdit.getThumbnailUrl() != null) {
            s3Manager.deleteObjectByUrl(videoEdit.getThumbnailUrl());
        }

        videoEditRepository.delete(videoEdit);
    }

    @Override
    @Transactional
    public VideoEdit toggle(Long videoEditId, Long userId) {
        VideoEdit videoEdit = videoEditRepository.findById(videoEditId);
        VideoEdit.validateOwnership(videoEdit, userId);

        videoEdit.toggleBookmark();
        return videoEditRepository.save(videoEdit);
    }

    @Override
    public List<VideoEditSummary> getDailySummary(SearchSummaryVideoEditCommand command) {
        return videoEditRepository.findDailySummary(command);
    }

}
