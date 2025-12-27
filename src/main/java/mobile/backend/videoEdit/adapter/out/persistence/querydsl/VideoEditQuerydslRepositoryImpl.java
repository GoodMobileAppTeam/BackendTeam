package mobile.backend.videoEdit.adapter.out.persistence.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mobile.backend.videoEdit.adapter.out.persistence.entity.QVideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.application.service.Cursor;
import mobile.backend.videoEdit.domain.command.ScrollDirection;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEditQuerydslRepositoryImpl implements VideoEditQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QVideoEditEntity v = QVideoEditEntity.videoEditEntity;

    @Override
    public List<VideoEditEntity> search(SearchVideoEditCommand command, Pageable pageable) {

        return queryFactory
                .selectFrom(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.onlyBookMarked()),
                        initCondition(command),
                        cursorCondition(command)
                )
                .orderBy(orderSpecifiers(command.direction()))
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression bookmarkCondition(boolean onlyBookMarked) {
        return onlyBookMarked ? v.isBookMark.isTrue() : null;
    }

    private BooleanExpression initCondition(SearchVideoEditCommand command) {
        if (command.direction() != ScrollDirection.INIT) return null;
        return v.saveTime.loe(command.baseDateEnd());
    }

    private BooleanExpression cursorCondition(SearchVideoEditCommand command) {

        if (command.direction() == ScrollDirection.INIT) return null;

        Cursor c = command.cursor();

        if (command.direction() == ScrollDirection.DOWN) {
            return v.saveTime.lt(c.saveTime())
                    .or(v.saveTime.eq(c.saveTime())
                            .and(v.createdAt.lt(c.createdAt())
                                    .or(v.createdAt.eq(c.createdAt())
                                            .and(v.id.lt(c.id())))));
        }

        return v.saveTime.gt(c.saveTime())
                .or(v.saveTime.eq(c.saveTime())
                        .and(v.createdAt.gt(c.createdAt())
                                .or(v.createdAt.eq(c.createdAt())
                                        .and(v.id.gt(c.id())))));
    }

    private OrderSpecifier<?>[] orderSpecifiers(ScrollDirection direction) {

        if (direction == ScrollDirection.UP) {
            return new OrderSpecifier[]{
                    v.saveTime.asc(),
                    v.createdAt.asc(),
                    v.id.asc()
            };
        }

        return new OrderSpecifier[]{
                v.saveTime.desc(),
                v.createdAt.desc(),
                v.id.desc()
        };
    }
}
