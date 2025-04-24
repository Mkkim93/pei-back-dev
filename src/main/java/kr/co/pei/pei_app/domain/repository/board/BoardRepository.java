package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.domain.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select count(b) from Board b where b.id in :boardIds")
    long countByIdIn(@Param("boardIds") List<Long> boardIds);

    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id = :boardId")
    Integer updateView(@Param("boardId") Long boardId);
}
