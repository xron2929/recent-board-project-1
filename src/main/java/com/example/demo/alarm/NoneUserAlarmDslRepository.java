package com.example.demo.alarm;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.demo.alarm.QNoneUserAlarm.noneUserAlarm;

@Repository
public class NoneUserAlarmDslRepository {

    @PersistenceContext
    EntityManager em;


    public List<AlarmDto> findByIdAndQuantity (Long startId, Long boardQuantity, String userName) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        System.out.println("startId = " + startId);
        System.out.println("boardQuantity = " + boardQuantity);
        return queryFactory.select(Projections.constructor(AlarmDto.class,
                        noneUserAlarm.id,noneUserAlarm.title,noneUserAlarm.boardId,
                        noneUserAlarm.summaryCommentContent,noneUserAlarm.commentWriter,
                        noneUserAlarm.isVisited,noneUserAlarm.boardWriterId
                ))
                .where(
                        noneUserAlarm.userName.eq(userName)
                )
                .from(noneUserAlarm)
                .limit(boardQuantity)
                .offset(startId)
                .fetch();
    }

}


