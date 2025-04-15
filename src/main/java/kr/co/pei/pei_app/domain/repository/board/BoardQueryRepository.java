package kr.co.pei.pei_app.domain.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;
import kr.co.pei.pei_app.domain.entity.board.Board;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static kr.co.pei.pei_app.domain.entity.board.QBoard.*;
import static kr.co.pei.pei_app.domain.entity.users.QUsers.users;

@Repository
public class BoardQueryRepository extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardQueryRepository(EntityManager em) {
        super(Board.class);
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public BoardDetailDTO detail(Long boardId) {

        return queryFactory.select(Projections.constructor(BoardDetailDTO.class,
                board.id,
                board.title,
                board.content,
                users.name,
                board.updatedAt,
                board.views,
                users.username))
                .from(board)
                .join(board.users, users)
                .where(board.id.eq(boardId))
                .fetchOne();
    }

    @Override
    public Long update(BoardUpdateDTO boardUpdateDTO) {

        JPAUpdateClause updateClause = new JPAUpdateClause(getEntityManager(), board);

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
}
