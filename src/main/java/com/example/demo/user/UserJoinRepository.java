package com.example.demo.user;

import com.example.demo.board.QBoard;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.defaultuser.QDefaultMember;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.noneuser.QNoneMember;
import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.oauthuser.QOauthMember;
import com.example.demo.user.siteuser.QSiteMember;
import com.example.demo.user.siteuser.SiteMember;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.demo.authority.QAuthority.authority;
import static com.example.demo.board.QBoard.board;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.userAuthority.QUserAuthority.userAuthority;
import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
public class
UserJoinRepository {
    private final EntityManager em;
    public String findDefaultMemberByBoardId(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(defaultMember.userId)
                .from(defaultMember)
                .rightJoin(defaultMember.boards, QBoard.board)
                .where(QBoard.board.id.eq(boardId))
                .fetchOne();
    }
    public String  findNoneMemberByBoardId(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(QNoneMember.noneMember.uuid)
                .from(QNoneMember.noneMember)
                .rightJoin(QNoneMember.noneMember.boards,QBoard.board)
                // 멤버끼리 연결하려고 하지말고
                .where(QBoard.board.id.eq(boardId))
                .fetchOne();
    }

    public SiteMember findBySiteMemberId(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(QSiteMember.siteMember)
                .from(QSiteMember.siteMember)
                .innerJoin(QSiteMember.siteMember.userAuthorities, userAuthority)
                .where(QSiteMember.siteMember.userId.eq(memberId))
                .fetchOne();
    }

    public DefaultMember findBymember(String memberId) {
        System.out.println("memberId = " + memberId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .select(defaultMember)
                .from(defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .where(defaultMember.userId.eq(memberId))
                .fetchOne();
        // 걍 쿼리 3번 썼음 최적화 할꺼면 튜플로 jdbc 써야되서 이거는 몽고 db 전환 생각
    }
    public List<DefaultMember> findByMemberId(String memberId, Long limitCount) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(defaultMember)
                .from(defaultMember)
                .where(defaultMember.userId.eq(memberId))
                .limit(limitCount)
                .fetch();
    }

    public List<NoneMember> findByNoneMemberId(String memberId, Long limitCount) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(QNoneMember.noneMember)
                .from(QNoneMember.noneMember)
                .where(QNoneMember.noneMember.userId.eq(memberId))
                .limit(limitCount)
                .fetch();
    }
    public long getAdminCount(String userId) {
        System.out.println("getAdminCount - userId = " + userId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(defaultMember.count())
                .from(defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .join(userAuthority.authority,authority)
                .where(defaultMember.userId.isNull().not()
                        .and(defaultMember.userId.eq(userId))
                        .and(authority.authorityName.eq("ROLE_ADMIN")))
                .fetchOne();
    }


    public DefaultMember findByDefaultMember(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(defaultMember)
                .from(defaultMember)
                .where(defaultMember.userId.eq(memberId))
                .fetchOne();
    }


    public int findByMemberCount(String memberId,Long limitCount) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(defaultMember)
                .from(defaultMember)
                .where(defaultMember.userId.eq(memberId))
                .limit(limitCount)
                .fetch().size();
    }
    public void saveMember(Long id,String memberId,String memberPassword) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.update(defaultMember)
                .set(defaultMember.userId,memberId)
                .set(defaultMember.password,memberPassword)
                .where(defaultMember.id.eq(id)).execute();
    }

    public String findPassword(String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(defaultMember.password)
                .where(defaultMember.userId.eq(userId))
                .from(defaultMember)
                .fetchOne();

    }


    public UserIdAndPasswordDto findUserIdAndPassword(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(
                        Projections.constructor(UserIdAndPasswordDto.class, defaultMember.userId, defaultMember.password))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.id.eq(boardId)).fetchOne();
    }

    public String findByPhoneNumberOrNickname(String phoneNumber, String nickname) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Expression<String> duplicatePhoneNumber = constant("duplicatePhoneNumber");
        Expression<String> duplicateNickname = constant("duplicateNickname");
        Expression<String> duplicatePhoneNumberAndNickname = constant("duplicatePhoneNumberAndNickname");

        return queryFactory
                .select(
                        Expressions.cases()
                                .when(QDefaultMember.defaultMember.phoneNumber.eq(phoneNumber).and(QDefaultMember.defaultMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(QDefaultMember.defaultMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .otherwise(duplicateNickname)
                )
                .from(QDefaultMember.defaultMember)
                .where(QDefaultMember.defaultMember.phoneNumber.eq(phoneNumber).or(QDefaultMember.defaultMember.nickname.eq(nickname)))
                .fetchOne();
    }



}
