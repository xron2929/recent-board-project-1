package com.example.demo.board;

import com.example.demo.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    @EntityGraph(attributePaths = {"author"})
    List<Board> findAll();
    /*
    @Query(value = "SELECT b.board_id as boredId,b.title as title,b.content as contents,u.user_id as username,b.created_date as createdDate,b.last_modified_date as lastModifiedDate " +
            "FROM board b JOIN default_member u ON b.member_id = u.id  order by board_id DESC limit :startId,:boardQuantity",nativeQuery = true)
    public List<MemberBoardQueryDTOInterface>findByUserIds(@Param("startId")Long startId, @Param("boardQuantity") Long boardQuantity);

     */

}
