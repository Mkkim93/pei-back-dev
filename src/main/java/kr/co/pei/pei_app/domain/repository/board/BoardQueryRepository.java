package kr.co.pei.pei_app.domain.repository.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindTempDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardUpdateDTO;
import kr.co.pei.pei_app.admin.application.dto.board.*;
import kr.co.pei.pei_app.admin.application.dto.file.QAdminDetailFileBoardDTO;

import kr.co.pei.pei_app.admin.application.exception.board.BoardDeleteFailedException;
import kr.co.pei.pei_app.domain.entity.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static kr.co.pei.pei_app.domain.entity.board.QBoard.*;
import static kr.co.pei.pei_app.domain.entity.file.QFileStore.*;
import static kr.co.pei.pei_app.domain.entity.users.QUsers.users;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public AdminBoardDetailDTO detail(Long boardId) {

        return queryFactory.selectFrom(board)
                .leftJoin(board.users, users)
                .leftJoin(fileStore).on(fileStore.board.id.eq(board.id)
                        .and(fileStore.used.eq(true)))
                .where(board.id.eq(boardId).and(board.isTemp.isFalse()))
                .transform(groupBy(board.id).as(new QAdminBoardDetailDTO(
                                        board.id,
                                        board.title,
                                        board.content,
                                        board.users.name,
                                        board.updatedAt,
                                        board.views,
                                        users.username,
                                        users.id,
                                        list(new QAdminDetailFileBoardDTO(
                                                        fileStore.id,
                                                        fileStore.name,
                                                        fileStore.path,
                                                        fileStore.orgName,
                                                        fileStore.board.id,
                                                        fileStore.renderType,
                                                        fileStore.used
                                                )
                                        )
                                )
                        )
                ).get(boardId);
    }

    @Override
    public Long update(AdminBoardUpdateDTO adminBoardUpdateDTO) {

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, board);

        if (adminBoardUpdateDTO.getTitle() != null) {
            updateClause.set(board.title, adminBoardUpdateDTO.getTitle());
        }
        if (adminBoardUpdateDTO.getContent() != null) {
            updateClause.set(board.content, adminBoardUpdateDTO.getContent());
        }

        updateClause.set(board.updatedAt, LocalDateTime.now());
        updateClause.where(board.id.eq(adminBoardUpdateDTO.getId()));

        long updateCount = updateClause.execute();

        if (updateCount > 0) {
            return adminBoardUpdateDTO.getId();
        }
        throw new IllegalArgumentException("게시글 업데이트 실패");
    }

    @Override
    public Long delete(List<Long> boardId) throws BoardDeleteFailedException {

        return queryFactory.update(board)
                .set(board.isDeleted, true)
                .where(board.id.in(boardId))
                .execute();
    }

    @Override
    public Page<AdminBoardFindDTO> searchPageSimple(String searchKeyword, Pageable pageable) {
        // 카운트 쿼리 날리지 않기
        List<AdminBoardFindDTO> content = queryFactory.select(new QAdminBoardFindDTO(
                        board.id,
                        board.title,
                        board.createdAt,
                        board.updatedAt,
                        users.name,
                        users.roleType,
                        board.views,
                        users.id
                )).from(board)
                .leftJoin(board.users, users)
                .where(
                        board.isDeleted.isFalse(),
                        board.isTemp.isFalse(),
                        keywordEq(searchKeyword)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdAt.desc())
                .fetch();

        JPAQuery<Board> count = queryFactory
                .selectFrom(board)
                .leftJoin(board.users, users)
                .where(
                        board.isDeleted.isFalse(),
                        board.isTemp.isFalse(),
                        keywordEq(searchKeyword)
                );
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchCount());
    }

    @Override
    public Page<AdminBoardFindTempDTO> searchPageTemp(Long usersId, String searchKeyword, Pageable pageable) {

        List<AdminBoardFindTempDTO> content = queryFactory.select(new QAdminBoardFindTempDTO(
                        board.id,
                        board.title,
                        board.createdAt,
                        board.updatedAt,
                        users.id))
                .from(board)
                .leftJoin(board.users, users)
                .where(
                        board.isDeleted.isFalse(),
                        board.isTemp.isTrue(),
                        keywordEq(searchKeyword),
                        userIdEq(usersId)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdAt.desc())
                .fetch();

        JPAQuery<Board> count = queryFactory.selectFrom(board)
                .leftJoin(board.users, users)
                .where(
                        board.isDeleted.isFalse(),
                        board.isTemp.isTrue(),
                        keywordEq(searchKeyword),
                        userIdEq(usersId)
                );
        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchCount());
    }

    private BooleanExpression userIdEq(Long usersId) {
        return usersId != null ? users.id.eq(usersId) : null;
    }

    private BooleanExpression keywordEq(String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return null;
        }
        return board.title.containsIgnoreCase(searchKeyword);
    }
}
