package com.example.demo.user.oauthuser;

import com.example.demo.user.defaultuser.QDefaultMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OauthUserDslRepository {
    @PersistenceContext
    EntityManager em;
    public long getUserIdOrEmailOrPhoneNumber(String userId,String email,String phoneNumber) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(QOauthMember.oauthMember.count())
                .from(QOauthMember.oauthMember)
                .where(QOauthMember.oauthMember.userId.eq(userId)
                        .or(QOauthMember.oauthMember.email.eq(email))
                        .or(QOauthMember.oauthMember.phoneNumber.eq(phoneNumber))).fetchOne();
    }
}
