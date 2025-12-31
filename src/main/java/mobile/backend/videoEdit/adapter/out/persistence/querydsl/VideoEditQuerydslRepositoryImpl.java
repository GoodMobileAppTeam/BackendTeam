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
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEditQuerydslRepositoryImpl implements VideoEditQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QVideoEditEntity v = QVideoEditEntity.videoEditEntity;

    @Override
    public List<VideoEditEntity> search(SearchVideoEditCommand command, int sizePlusOne) {

        return queryFactory
                .selectFrom(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        initCondition(command),
                        cursorCondition(command)
                )
                .orderBy(orderSpecifiers(command.direction()))
                .limit(sizePlusOne)
                .fetch();
    }

    @Override
    public boolean existsBefore(SearchVideoEditCommand command, VideoEditEntity first) {

        Integer result = queryFactory
                .selectOne()
                .from(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        v.saveTime.gt(first.getSaveTime())
                                .or(v.saveTime.eq(first.getSaveTime())
                                        .and(v.createdAt.gt(first.getCreatedAt()))
                                        .or(v.saveTime.eq(first.getSaveTime())
                                                .and(v.createdAt.eq(first.getCreatedAt()))
                                                .and(v.id.gt(first.getId()))))
                )
                .limit(1)
                .fetchFirst();

        return result != null;
    }

    @Override
    public boolean existsAfter(SearchVideoEditCommand command, VideoEditEntity last) {

        Integer result = queryFactory
                .selectOne()
                .from(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        v.saveTime.lt(last.getSaveTime())
                                .or(v.saveTime.eq(last.getSaveTime())
                                        .and(v.createdAt.lt(last.getCreatedAt()))
                                        .or(v.saveTime.eq(last.getSaveTime())
                                                .and(v.createdAt.eq(last.getCreatedAt()))
                                                .and(v.id.lt(last.getId()))))
                )
                .limit(1)
                .fetchFirst();

        return result != null;
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
