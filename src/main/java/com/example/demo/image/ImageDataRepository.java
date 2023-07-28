package com.example.demo.image;


import com.example.demo.board.QBoard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public class ImageDataRepository {
    @PersistenceContext
    EntityManager em;

    // 해당 날짜 vs 오늘 날짜
    // 해당 날짜로부터 유통기한 10시간
    // 해당날짜 + 10 시간 >= 오늘 날짜
    // 해당 날짜 >= 오늘날짜 - 10시간
    public List<ImageBoardDto> findExpiredImage() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        System.out.println("currentDateTime = " + currentDateTime);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return
                queryFactory.select(Projections.constructor(ImageBoardDto.class, QBoard.board.id,QImageStore.imageStore.filePath))
                        .from(QImageStore.imageStore)
                        .rightJoin(QImageStore.imageStore.board,QBoard.board)
                        // 이거 항상 select가 다에서 1만 되는데 right join이 안되면
                        // Querydsl이 작성안되는 경우라는 거 기록해야될듯
                        .where(QBoard.board.createdDate.loe(LocalDateTime.now().minusMinutes(1)))
                        .fetch();
        // .where(Expressions.currentTimestamp()
        //     .after(LocalDateTime.now().plusSeconds(10)));

    }
    @Transactional
    public void delete(Set<Long> boardIds) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(QImageStore.imageStore)
                .where(QImageStore.imageStore.board.id.in(boardIds))
                .execute();
    }
    public List<ImageBoardDto> findImage(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return
                queryFactory.select(Projections.constructor(ImageBoardDto.class,QBoard.board.id,QImageStore.imageStore.filePath))
                        .from(QImageStore.imageStore)
                        .rightJoin(QImageStore.imageStore.board,QBoard.board)
                        .where(QBoard.board.id.eq(boardId)).fetch();
    }
}

