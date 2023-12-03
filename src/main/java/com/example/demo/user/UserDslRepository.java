package com.example.demo.user;


import com.example.demo.board.QBoard;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.noneuser.QNoneMember;
import com.example.demo.user.siteuser.SiteMember;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.demo.board.QBoard.board;
import static com.example.demo.user.authority.QAuthority.authority;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.user.oauthuser.QOauthMember.oauthMember;
import static com.example.demo.user.siteuser.QSiteMember.siteMember;
import static com.example.demo.user.userAuthority.QUserAuthority.userAuthority;
import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
public class UserDslRepository {
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
                .select(QNoneMember.noneMember.userId)
                .from(QNoneMember.noneMember)
                .rightJoin(QNoneMember.noneMember.boards,QBoard.board)
                // 멤버끼리 연결하려고 하지말고
                .where(QBoard.board.id.eq(boardId))
                .fetchOne();
    }

    public SiteMember findBySiteMemberId(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(siteMember)
                .from(siteMember)
                .innerJoin(siteMember.userAuthorities, userAuthority)
                .where(siteMember.userId.eq(memberId))
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
    public String findUserIdByEmail(String email) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(siteMember.userId)
                .from(siteMember)
                .where(siteMember.email.eq(email)).fetchOne();
    }


    public List<String> findByPhoneNumberOrNickname(String phoneNumber, String nickname) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Expression<String> duplicatePhoneNumber = constant("해당 계정으로 가입한 전화번호가 이미 존재합니다");
        Expression<String> duplicateNickname = constant("중복된 닉네임이 이미 존재합니다");
        Expression<String> duplicatePhoneNumberAndNickname = constant("해당 계정으로 가입한 전화번호와 중복된 닉네임이 이미 존재합니다");
        return queryFactory
                .select(
                        Expressions.cases()
                                .when(siteMember.phoneNumber.eq(phoneNumber).and(siteMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(oauthMember.phoneNumber.eq(phoneNumber).and(oauthMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(siteMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .when(siteMember.nickname.eq(nickname)).then(duplicateNickname)
                                .when(oauthMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .when(oauthMember.nickname.eq(nickname)).then(duplicateNickname)
                                .otherwise("")
                )
                .from(defaultMember)
                .leftJoin(siteMember).on(defaultMember.id.eq(siteMember.id))
                .leftJoin(oauthMember).on(defaultMember.id.eq(oauthMember.id))
                .fetch();



    }
    /*
    아래는 작동안함
     */
    public List<String> findByPhoneNumberOrNickname2(String phoneNumber, String nickname) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Expression<String> duplicatePhoneNumber = constant("duplicatePhoneNumber");
        Expression<String> duplicateNickname = constant("duplicateNickname");
        Expression<String> duplicatePhoneNumberAndNickname = constant("duplicatePhoneNumberAndNickname");
        return queryFactory
                .select(
                        Expressions.cases()
                                .when(siteMember.phoneNumber.eq(phoneNumber).and(siteMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(oauthMember.phoneNumber.eq(phoneNumber).and(oauthMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(siteMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .when(siteMember.nickname.eq(nickname)).then(duplicateNickname)
                                .when(oauthMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .when(oauthMember.nickname.eq(nickname)).then(duplicateNickname)
                                .otherwise("")
                )
                .from(defaultMember)
                .join(siteMember).on(defaultMember.id.eq(siteMember.id))
                .join(oauthMember).on(defaultMember.id.eq(oauthMember.id))
                .fetch();



    }
    public void changeUserPasswordByEmailAndUserId(String changePassword,String email,String userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.update(siteMember).set(siteMember.password,changePassword)
                .where(siteMember.userId.eq(userId).and(siteMember.email.eq(email))).execute();

    }


}

