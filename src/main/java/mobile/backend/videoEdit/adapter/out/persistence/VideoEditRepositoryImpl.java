package mobile.backend.videoEdit.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.jpa.VideoEditJpaRepository;
import mobile.backend.videoEdit.application.port.out.VideoEditRepository;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import mobile.backend.videoEdit.domain.model.VideoEdit;
import mobile.backend.videoEdit.exception.VideoErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
    public Page<VideoEdit> search(SearchVideoEditCommand criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size());

        Page<VideoEditEntity> entityPage = jpaRepository.search(
                criteria.userId(),
                criteria.year(),
                criteria.month(),
                criteria.isBookMarked(),
                pageable
        );

        return entityPage.map(VideoEditEntity::toDomain);
    }

    @Override
    public void delete(VideoEdit videoEdit) {
        jpaRepository.deleteById(videoEdit.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
