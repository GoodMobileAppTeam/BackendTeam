package mobile.backend.videoEdit.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoDailySummaryProjection;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoEditJpaRepository;
import mobile.backend.videoEdit.adapter.out.persistence.querydsl.VideoEditQuerydslRepository;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.application.service.CursorPageResult;
import mobile.backend.videoEdit.domain.command.ScrollDirection;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEditRepositoryImpl implements VideoEditRepository {

    private final VideoEditJpaRepository jpaRepository;
    private final VideoEditQuerydslRepository querydslRepository;

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
    public CursorPageResult<VideoEdit> search(SearchVideoEditCommand command) {

        int sizePlusOne = command.size() + 1;

        List<VideoEditEntity> entities = querydslRepository.search(command, sizePlusOne);

        boolean hasNext = entities.size() > command.size();

        if (hasNext) {
            entities = entities.subList(0, command.size());
        }

        if (command.direction() == ScrollDirection.UP) {
            Collections.reverse(entities);
        }

        boolean hasPrev = !entities.isEmpty() && querydslRepository.existsBefore(command, entities.get(0));

        boolean hasAfter = !entities.isEmpty() && querydslRepository.existsAfter(command, entities.get(entities.size() - 1));

        return new CursorPageResult<>(
                entities.stream()
                        .map(VideoEditEntity::toDomain)
                        .toList(),
                hasAfter,
                hasPrev
        );
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
