package com.example.demo.board;

import com.example.demo.board.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.example.demo.board.QBoard.board;

@Repository
public class BoardDslRepository {
    @PersistenceContext
    private EntityManager em;


    public void updateBoard(Long boardId, String title,
                            String content) {
        System.out.println("boardId = " + boardId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        queryFactory.update(board)
                .set(board.content,content)
                .set(board.title,title)
                .where(board.id.eq(boardId)).execute();
    }

    public Long selectFindByIdPage(String keyword) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(board.content.count())
                .from(board)
                .where(board.content.like(keyword))
                .fetchOne();
    }
    public Long findBoardCount() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(board.content.count())
                .from(board)
                .where(board.isSecret.eq(false))
                .fetchOne();
    }
    public Long findFinalBoard() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(board.id)
                .from(board)
                .orderBy(board.id.desc())
                .limit(1)
                .fetchOne();

    }

    public void deleteById(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory
                .delete(board)
                .where(board.id.eq(id))
                .execute();
    }

    public void deleteByIds(Set<Board> boards) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory
                .delete(board)
                .where(board.in(boards))
                .execute();
    }





}
