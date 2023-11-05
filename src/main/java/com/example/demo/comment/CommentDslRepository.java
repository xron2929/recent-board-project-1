package com.example.demo.comment;


import com.example.demo.board.QBoard;
import com.example.demo.entityjoin.CommentSaveDataDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

import static com.example.demo.comment.QParentComment.parentComment;

@Repository
public class CommentDslRepository {
    @PersistenceContext
    EntityManager em;

    public void saveParentComment(CommentSaveDataDto commentInsertDataDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.insert(parentComment)
                .set(parentComment.member.id, commentInsertDataDto.getMember().getId())
                .set(parentComment.board.id, commentInsertDataDto.getBoard().getId())
                .set(parentComment.content, commentInsertDataDto.getContent())
                .execute();
                /*
                .set(QImageStore.imageStore.createdDate, imageStore.getCreatedDate())
                .set(QImageStore.imageStore.board, imageStore.getBoard())
                .set(QImageStore.imageStore.filePath, imageStore.getFilePath());
                 */

    }

    @Transactional
    public void deleteParentComment(Set<Long> boardIds) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(parentComment)
                .where(parentComment.board.id.in(boardIds))
                .execute();
    }

    public List<Long>findByIdsSubQueryParentComment(Long startId,Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        System.out.println("startId = " + startId);
        System.out.println("boardId = " + boardId);
        return queryFactory
                .select(parentComment.id)
                .from(parentComment)
                .leftJoin(parentComment.board,QBoard.board)
                .where(QBoard.board.id.eq(boardId))
                .limit(10l)
                .offset(startId)
                .fetch();
    }
}


