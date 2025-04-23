package kr.co.pei.pei_app.domain.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;

import kr.co.pei.pei_app.application.dto.board.QBoardDetailDTO;
import kr.co.pei.pei_app.application.dto.file.QFileDetailBoardDTO;

import kr.co.pei.pei_app.application.exception.board.BoardDeleteFailedException;
import lombok.RequiredArgsConstructor;
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
    public BoardDetailDTO detail(Long boardId) {

        return queryFactory.selectFrom(board)
                .leftJoin(board.users, users)
                .leftJoin(fileStore).on(fileStore.board.id.eq(board.id)
                        .and(fileStore.used.eq(true)))
                .where(board.id.eq(boardId))
                .transform(groupBy(board.id).as(new QBoardDetailDTO(
                                        board.id,
                                        board.title,
                                        board.content,
                                        board.users.name,
                                        board.updatedAt,
                                        board.views,
                                        users.username,
                                        users.id,
                                        list(new QFileDetailBoardDTO(
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
    public Long update(BoardUpdateDTO boardUpdateDTO) {

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, board);

        if (boardUpdateDTO.getTitle() != null) {
            updateClause.set(board.title, boardUpdateDTO.getTitle());
        }
        if (boardUpdateDTO.getContent() != null) {
            updateClause.set(board.content, boardUpdateDTO.getContent());
        }

        updateClause.set(board.updatedAt, LocalDateTime.now());
        updateClause.where(board.id.eq(boardUpdateDTO.getId()));

        long updateCount = updateClause.execute();

        if (updateCount > 0) {
            return boardUpdateDTO.getId();
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
}
