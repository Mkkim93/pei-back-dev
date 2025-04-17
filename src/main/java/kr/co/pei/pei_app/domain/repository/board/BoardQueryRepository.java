package kr.co.pei.pei_app.domain.repository.board;


import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;

import kr.co.pei.pei_app.application.dto.board.QBoardDetailDTO;
import kr.co.pei.pei_app.application.dto.file.FileDetailBoardDTO;
import kr.co.pei.pei_app.application.dto.file.QFileDetailBoardDTO;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.board.QBoard;

import kr.co.pei.pei_app.domain.entity.file.QFileStore;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


import static kr.co.pei.pei_app.domain.entity.board.QBoard.*;
import static kr.co.pei.pei_app.domain.entity.file.QFileStore.*;
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

        List<FileDetailBoardDTO> fileList = queryFactory
                .select(new QFileDetailBoardDTO(
                        fileStore.id,
                        fileStore.name,
                        fileStore.path,
                        fileStore.orgName,
                        fileStore.board.id,
                        fileStore.renderType))
                .from(fileStore)
                .where(fileStore.board.id.eq(boardId))
                .fetch();

        BoardDetailDTO boardDetailDTO = queryFactory
                .select(new QBoardDetailDTO(
                        board.id,
                        board.title,
                        board.content,
                        board.users.name,
                        board.updatedAt,
                        board.views,
                        users.username))
                .from(board)
                .leftJoin(board.users, users)
                .where(board.id.eq(boardId))
                .fetchOne();

        if (boardDetailDTO != null) {
            boardDetailDTO.setBoardFiles(fileList);
        }

        return boardDetailDTO;
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
