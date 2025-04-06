package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.application.dto.board.BoardFindDTO;
import kr.co.pei.pei_app.domain.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            select new kr.co.pei.pei_app.application.dto.board.BoardFindDTO(
            b.id, b.title, b.content, b.createdAt, b.updatedAt, b.users.name, b.users.roleType, b.views
            )
            from Board b
            join b.users u
            where b.title like concat('%', :keyword, '%') 
            or b.content like concat('%', :keyword, '%')
           """)
    Page<BoardFindDTO> searchByBoardPages(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        select new kr.co.pei.pei_app.application.dto.board.BoardFindDTO(
        b.id, b.title, b.content, b.createdAt, b.updatedAt, b.users.name, b.users.roleType, b.views) 
        from Board b
        join b.users u
    """)
    Page<BoardFindDTO> findBoardDTOS(Pageable pageable);
}
