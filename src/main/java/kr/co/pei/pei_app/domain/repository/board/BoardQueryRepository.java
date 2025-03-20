package kr.co.pei.pei_app.domain.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.board.DetailBoardDTO;
import kr.co.pei.pei_app.application.dto.board.UpdateBoardDTO;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.board.QBoard;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static kr.co.pei.pei_app.domain.entity.board.QBoard.*;
import static kr.co.pei.pei_app.domain.entity.users.QUsers.*;

@Repository
public class BoardQueryRepository extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardQueryRepository(EntityManager em) {
        super(Board.class);
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public DetailBoardDTO detail(Long boardId) {

        Board board = queryFactory.selectFrom(QBoard.board)
                .leftJoin(QBoard.board.users, users).fetchJoin()
                .where(QBoard.board.id.eq(boardId))
                .fetchOne();

        return new DetailBoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUsers().getName(),
                board.getUpdatedAt(),
                board.getViews(),
                board.getUsers().getUsername());
    }

    @Override
    public Boolean update(UpdateBoardDTO updateBoardDTO) {

        JPAUpdateClause updateClause = new JPAUpdateClause(getEntityManager(), board);

        if (updateBoardDTO.getTitle() != null) {
            updateClause.set(board.title, updateBoardDTO.getTitle());
        }
        if (updateBoardDTO.getContent() != null) {
            updateClause.set(board.content, updateBoardDTO.getContent());
        }

        updateClause.set(board.updatedAt, LocalDateTime.now());
        updateClause.where(board.id.eq(updateBoardDTO.getId()));

        long updateCount = updateClause.execute();

        if (updateCount > 0) {
            return true;
        }
        return false;
    }
}
