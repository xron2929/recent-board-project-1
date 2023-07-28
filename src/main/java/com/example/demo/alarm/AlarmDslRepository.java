package com.example.demo.alarm;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.demo.alarm.QAlarm.alarm;

@Repository
public class AlarmDslRepository {
    @PersistenceContext
    EntityManager em;

    public List<AlarmDto> findByIdAndQuantity (Long startId, Long boardQuantity, String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        System.out.println("startId = " + startId);
        System.out.println("boardQuantity = " + boardQuantity);
        return queryFactory.select(Projections.constructor(AlarmDto.class,
                        alarm.id, alarm.title,alarm.boardId,alarm.summaryCommentContent,
                        alarm.commentWriter,alarm.isVisited,alarm.boardWriterId))
                .where(alarm.boardWriterId.eq(userId))
                .from(alarm)
                .limit(boardQuantity)
                .offset(startId)
                .fetch();
    }

}

