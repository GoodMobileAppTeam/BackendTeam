package mobile.backend.videoEdit.adapter.out.persistence.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mobile.backend.videoEdit.adapter.out.persistence.entity.QVideoEditEntity;
import mobile.backend.videoEdit.adapter.out.persistence.entity.VideoEditEntity;
import mobile.backend.videoEdit.application.service.querymodel.Cursor;
import mobile.backend.videoEdit.domain.command.ScrollDirection;
import mobile.backend.videoEdit.domain.command.SearchVideoEditCommand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEditQuerydslRepositoryImpl implements VideoEditQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QVideoEditEntity v = QVideoEditEntity.videoEditEntity;

    /**
     * 영상 목록 조회
     *
     * [쿼리 의미]
     * - userId 기준
     * - (옵션) 북마크 필터
     * - INIT: baseDateEnd 이전 데이터 조회
     * - UP/DOWN: cursor 기준으로 newer / older 데이터 조회
     * - 정렬은 항상 최신 → 과거 (DESC)
     */
    @Override
    public List<VideoEditEntity> search(SearchVideoEditCommand command) {

        return queryFactory
                .selectFrom(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        initCondition(command),
                        cursorCondition(command)
                )
                .orderBy(orderByDesc())
                .limit(command.size())
                .fetch();
    }

    /**
     * hasNext 판단용
     *
     * 의미:
     * - 현재 페이지 마지막 요소보다
     * - "더 오래된 데이터"가 존재하는지
     *
     * SQL 개념:
     * WHERE (save_time, created_at, id) < (last)
     * LIMIT 1
     */
    @Override
    public boolean existsOlder(SearchVideoEditCommand command, VideoEditEntity last) {

        Integer result = queryFactory
                .selectOne()
                .from(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        olderThan(last)
                )
                .limit(1)
                .fetchFirst();

        return result != null;
    }

    /**
     * hasPrev 판단용
     *
     * 의미:
     * - 현재 페이지 첫 요소보다
     * - "더 최신 데이터"가 존재하는지
     *
     * SQL 개념:
     * WHERE (save_time, created_at, id) > (first)
     * LIMIT 1
     */
    @Override
    public boolean existsNewer(SearchVideoEditCommand command, VideoEditEntity first) {

        Integer result = queryFactory
                .selectOne()
                .from(v)
                .where(
                        v.userId.eq(command.userId()),
                        bookmarkCondition(command.bookMarkApi()),
                        newerThan(first)
                )
                .limit(1)
                .fetchFirst();

        return result != null;
    }

    /* =========================
       조건 분리 영역
       ========================= */

    private BooleanExpression bookmarkCondition(boolean onlyBookMarked) {
        return onlyBookMarked ? v.isBookMark.isTrue() : null;
    }

    /**
     * INIT 요청 처리
     *
     * 의미:
     * - 최신 조회가 아님
     * - baseDateEnd 이전 데이터를 기준으로 조회
     */
    private BooleanExpression initCondition(SearchVideoEditCommand command) {
        if (command.direction() != ScrollDirection.INIT) {
            return null;
        }
        return v.userDefinedDate.loe(command.baseDateEnd());
    }

    /**
     * 커서 기반 조회 조건
     *
     * DOWN → older
     * UP   → newer
     */
    private BooleanExpression cursorCondition(SearchVideoEditCommand command) {

        if (command.direction() == ScrollDirection.INIT) {
            return null;
        }

        Cursor c = command.cursor();

        return command.direction() == ScrollDirection.DOWN
                ? olderThan(c)
                : newerThan(c);
    }

    /**
     * cursor 보다 오래된 데이터
     * (DESC 정렬 기준)
     */
    private BooleanExpression olderThan(Cursor c) {
        return v.userDefinedDate.lt(c.userDefinedDate())
                .or(v.userDefinedDate.eq(c.userDefinedDate())
                        .and(v.createdAt.lt(c.createdAt())))
                .or(v.userDefinedDate.eq(c.userDefinedDate())
                        .and(v.createdAt.eq(c.createdAt()))
                        .and(v.id.lt(c.id())));
    }

    private BooleanExpression olderThan(VideoEditEntity e) {
        return olderThan(new Cursor(
                e.getUserDefinedDate(),
                e.getCreatedAt(),
                e.getId()
        ));
    }

    /**
     * cursor 보다 최신 데이터
     */
    private BooleanExpression newerThan(Cursor c) {
        return v.userDefinedDate.gt(c.userDefinedDate())
                .or(v.userDefinedDate.eq(c.userDefinedDate())
                        .and(v.createdAt.gt(c.createdAt())))
                .or(v.userDefinedDate.eq(c.userDefinedDate())
                        .and(v.createdAt.eq(c.createdAt()))
                        .and(v.id.gt(c.id())));
    }

    private BooleanExpression newerThan(VideoEditEntity e) {
        return newerThan(new Cursor(
                e.getUserDefinedDate(),
                e.getCreatedAt(),
                e.getId()
        ));
    }

    private OrderSpecifier<?>[] orderByDesc() {
        return new OrderSpecifier[]{
                v.userDefinedDate.desc(),
                v.createdAt.desc(),
                v.id.desc()
        };
    }
}
