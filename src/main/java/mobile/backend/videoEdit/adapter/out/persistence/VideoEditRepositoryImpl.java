package mobile.backend.videoEdit.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoDailySummaryProjection;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoEditJpaRepository;
import mobile.backend.videoEdit.adapter.out.persistence.querydsl.VideoEditQuerydslRepository;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.application.service.querymodel.CursorPageResult;
import mobile.backend.videoEdit.domain.command.SearchSummaryVideoEditCommand;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.domain.model.VideoEditSummary;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    public List<VideoEdit> getDailyVideos(Long userId, LocalDate userDefinedDate) {
        return jpaRepository
                .findAllByUserIdAndUserDefinedDateOrderByCreatedAtDesc(userId, userDefinedDate)
                .stream()
                .map(VideoEditEntity::toDomain)
                .toList();
    }

    @Override
    public CursorPageResult<VideoEdit> search(SearchVideoEditCommand command) {

        List<VideoEditEntity> entities = querydslRepository.search(command);

        boolean hasPrev = false;
        boolean hasNext = false;

        if (!entities.isEmpty()) {
            VideoEditEntity first = entities.get(0);
            VideoEditEntity last = entities.get(entities.size() - 1);

            // hasPrev = 더 최신 데이터 존재
            hasPrev = querydslRepository.existsNewer(command, first);

            // hasNext = 더 오래된 데이터 존재
            hasNext = querydslRepository.existsOlder(command, last);
        }

        return new CursorPageResult<>(
                entities.stream()
                        .map(VideoEditEntity::toDomain)
                        .toList(),
                hasNext,
                hasPrev
        );
    }

    @Override
    public void delete(VideoEdit videoEdit) {
        jpaRepository.deleteById(videoEdit.getId());
    }

    @Override
    public List<VideoEditSummary> findDailySummary(SearchSummaryVideoEditCommand command) {

        List<VideoDailySummaryProjection> results =
                jpaRepository.findDailySummary(
                        command.userId(),
                        command.startDate(),
                        command.endDate()
                );

        return results.stream()
                .map(VideoEditSummary::fromProjection)
                .toList();
    }

    @Override
    public int countByUserIdAndUserDefinedDate(Long userId, LocalDate userDefinedDate) {
        return jpaRepository.countByUserIdAndUserDefinedDate(userId, userDefinedDate);
    }
}
