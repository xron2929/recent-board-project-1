package com.example.demo.feedback;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.example.demo.feedback.QUserDisLike.userDisLike;

@Repository
public class UserDisLikeDslRepository {
    @PersistenceContext
    EntityManager em;

    public String getDisLikeCount(JoinStatus joinStatus,Long joinId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(userDisLike.count().stringValue())
                .where(userDisLike.joinStatus.eq(joinStatus).and(userDisLike.joinId.eq(joinId)))
                .from(userDisLike)
                .fetchOne();
    }

    public Long getDisLikeCount(JoinStatus joinStatus,Long joinId,String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(userDisLike.count())
                .where(userDisLike.joinStatus.eq(joinStatus).and(userDisLike.joinId.eq(joinId))
                        .and(userDisLike.userId.eq(userId)))
                .from(userDisLike)
                .fetchOne();
    }
    public void deleteDisLikeAccount(JoinStatus joinStatus,Long joinId,String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(userDisLike)
                .where(userDisLike.joinStatus.eq(joinStatus).and(userDisLike.joinId.eq(joinId))
                        .and(userDisLike.userId.eq(userId)))
                .execute();
    }

}
