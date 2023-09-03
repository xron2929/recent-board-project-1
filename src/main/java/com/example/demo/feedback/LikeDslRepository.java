package com.example.demo.feedback;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.example.demo.feedback.QUserDisLike.userDisLike;
import static com.example.demo.feedback.QUserLike.userLike;

@Repository
public class LikeDslRepository {
    @PersistenceContext
    EntityManager em;
    public Long getLikeCount(JoinStatus joinStatus,Long joinId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(userLike.count())
                .where(userLike.joinStatus.eq(joinStatus).and(userLike.joinId.eq(joinId)))
                .from(userLike)
                .fetchOne();
    }

    public boolean isExistanceDisLike(JoinStatus joinStatus,Long joinId,String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Long disLikeCount = queryFactory.select(userLike.count())
                .where(userLike.joinStatus.eq(joinStatus).and(userLike.joinId.eq(joinId))
                        .and(userLike.userId.eq(userId)))
                .from(userLike)
                .fetchOne();
        if(disLikeCount>0) {
            return true;
        }
        return false;
    }
    public void deleteLikeAccount(JoinStatus joinStatus,Long joinId,String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(userLike)
                .where(userLike.joinStatus.eq(joinStatus).and(userLike.joinId.eq(joinId))
                        .and(userLike.userId.eq(userId)))
                .execute();
    }
}
