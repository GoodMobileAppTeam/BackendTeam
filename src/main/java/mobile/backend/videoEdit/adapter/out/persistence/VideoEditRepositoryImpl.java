package mobile.backend.videoEdit.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoDailySummaryProjection;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoEditJpaRepository;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.SearchBookmarkVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEditRepositoryImpl implements VideoEditRepository {

    private final VideoEditJpaRepository jpaRepository;

    @Override
    public VideoEdit save(VideoEdit videoEdit) {
        VideoEditEntity entity = VideoEditEntity.toEntity(videoEdit);
        VideoEditEntity savedEntity = jpaRepository.save(entity);
        return VideoEditEntity.toDomain(savedEntity);
    }

    @Override
    public VideoEdit findById(Long id) {
        return jpaRepository.findById(id)
                .map(VideoEditEntity::toDomain)
                .orElseThrow(() -> new CustomException(VideoErrorCode.VIDEO_NOT_FOUND));
    }

    @Override
    public List<VideoEdit> search(SearchVideoEditCommand command) {

        Pageable pageable = PageRequest.of(0, command.size());

        List<VideoEditEntity> entities = switch (command.direction()) {

            case INIT -> jpaRepository.findInitPage(
                    command.userId(),
                    command.baseDateEnd(),
                    pageable
            );

            case DOWN -> jpaRepository.findNextPage(
                    command.userId(),
                    command.cursorSaveTime(),
                    command.cursorCreatedAt(),
                    command.cursorId(),
                    pageable
            );

            case UP -> {
                List<VideoEditEntity> result =
                        jpaRepository.findPrevPage(
                                command.userId(),
                                command.cursorSaveTime(),
                                command.cursorCreatedAt(),
                                command.cursorId(),
                                pageable
                        );
                yield reverse(result);
            }
        };

        return entities.stream()
                .map(VideoEditEntity::toDomain)
                .toList();
    }

    private List<VideoEditEntity> reverse(List<VideoEditEntity> list) {
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<VideoEdit> bookmarkSearch(SearchBookmarkVideoEditCommand command) {

        Pageable pageable = PageRequest.of(0, command.size());

        List<VideoEditEntity> entities = jpaRepository.findBookmarkedByCursor(
                        command.userId(),
                        command.cursorDate(),
                        command.cursorId(),
                        pageable
                );

        return entities.stream()
                .map(VideoEditEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(VideoEdit videoEdit) {
        jpaRepository.deleteById(videoEdit.getId());
    }

    @Override
    public List<VideoEditSummary> findDailySummary(SearchSummaryVideoEditCommand command) {

        List<VideoDailySummaryProjection> results = jpaRepository.findDailySummary(
                        command.userId(),
                        command.startDate(),
                        command.endDate()
                );

        return results.stream()
                .map(VideoEditSummary::fromProjection)
                .toList();
    }
}
