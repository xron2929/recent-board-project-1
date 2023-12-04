package com.example.demo.boradAndUser;

import com.example.demo.board.QBoard;
import com.example.demo.board.UserAuthorityAndUserIdDto;
import com.example.demo.boradAndUser.*;
import com.example.demo.entityjoin.NoneUserUuidANdTitleAndPasswordDto;
import com.example.demo.user.UserIdAndPasswordDto;
import com.example.demo.user.noneuser.QNoneMember;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.demo.board.QBoard.board;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.user.oauthuser.QOauthMember.oauthMember;
import static com.example.demo.user.siteuser.QSiteMember.siteMember;

import static com.example.demo.user.userAuthority.QUserAuthority.userAuthority;
import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
public class
UserJoinRepository {
    private final EntityManager em;

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



    public String findDefaultMemberByBoardId(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(defaultMember.userId)
                .from(defaultMember)
                .rightJoin(defaultMember.boards, QBoard.board)
                .where(QBoard.board.id.eq(boardId))
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

    public List<MemberBoardQueryDTO>findByUserIds(Long startId, Long boardQuantity) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(Projections.constructor(MemberBoardQueryDTO.class,
                        board.id, board.title,board.content, defaultMember.nickname,
                        board.createdDate,board.lastModifiedDate))
                .where(board.isSecret.eq(false))
                .from(board)
                .innerJoin(board.member,defaultMember)
                .orderBy(board.id.desc())
                .limit(boardQuantity)
                .offset(startId)
                .fetch();
    }
    public List<BoardSearchDataDto> selectFindById(Long PageNumber, Long BoardQuantity, String str) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        System.out.println("str = " + str);
        System.out.println("PageNumber = " + PageNumber);
        System.out.println("BoardQuantity = " + BoardQuantity);
        return queryFactory
                .select(Projections.constructor(BoardSearchDataDto.class,
                        board.id, board.title,
                        board.member.nickname,
                        board.createdDate))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.content.like(str).and(board.isSecret.eq(false)))
                .limit(BoardQuantity)
                .offset(PageNumber)
                .fetch();
    }
    public NoneUserBoardSaveDataDto findNoneUserEditBoard(Long boardId) {
        System.out.println("boardId = " + boardId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .select(Projections.constructor(NoneUserBoardSaveDataDto.class,
                        board.id,board.member.userId, board.title, board.content,
                        board.member.nickname, board.member.password,board.isSecret))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.id.eq(boardId).and(board.isSecret.eq(false)))
                .fetchOne();
    }
    public UserBoardSaveDataDto findUserEditBoard(Long boardId) {
        System.out.println("boardId = " + boardId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .select(Projections.constructor(UserBoardSaveDataDto.class,
                        board.member.userId))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.id.eq(boardId).and(board.isSecret.eq(false)))
                .fetchOne();
    }

    public String getAuthority(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(userAuthority.authority.authorityName)
                .from(board)
                .leftJoin(board.member, defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .where(board.id.eq(boardId))
                .limit(1)
                .fetchOne();
    }
    public UserAuthorityAndUserIdDto getAuthorityAndUserId(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(Projections.constructor(UserAuthorityAndUserIdDto.class,userAuthority.authority.authorityName,defaultMember.userId))
                .from(board)
                .leftJoin(board.member, defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .where(board.id.eq(boardId))
                .limit(1)
                .fetchOne();
    }
    //
    public TitleAndUserIdDto findUserDto(Long id) {
        // password랑 Id 검증 먼저 해야되는 거 아닌가?
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(Projections.constructor(TitleAndUserIdDto.class,
                        board.title,
                        defaultMember.userId
                        ,defaultMember.password))
                .from(board)
                .leftJoin(board.member,defaultMember)
                .where(board.id.eq(id))
                .fetchOne();
    }

    public NoneUserUuidANdTitleAndPasswordDto findNoneuserDto(Long id) {
        // password랑 Id 검증 먼저 해야되는 거 아닌가?
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(Projections.constructor(NoneUserUuidANdTitleAndPasswordDto.class, defaultMember.userId,
                        board.title,
                        defaultMember.userId
                        ,defaultMember.password))
                .from(board)
                .leftJoin(defaultMember).on(board.member.id.eq(defaultMember.id))
                .where(board.id.eq(id))
                .fetchOne();
    }
    public BoardResponseDto getBoardResponseDto(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(Projections.constructor(BoardResponseDto.class,
                        board.id, board.title, defaultMember.nickname,
                        board.content, board.createdDate, board.lastModifiedDate))
                .from(board)
                .leftJoin(board.member, defaultMember)
                .where(board.id.eq(boardId).and(board.isSecret.eq(false)))
                .fetchOne();
    }



    /*
    return queryFactory
                .select(
                        Expressions.cases()
                                .when(defaultMember.phoneNumber.eq(phoneNumber).and(defaultMember.nickname.eq(nickname))).then(duplicatePhoneNumberAndNickname)
                                .when(defaultMember.phoneNumber.eq(phoneNumber)).then(duplicatePhoneNumber)
                                .otherwise(duplicateNickname)
                )
                .from(defaultMember)
                .where(QDefaultMember.defaultMember.phoneNumber.eq(phoneNumber).or(QDefaultMember.defaultMember.nickname.eq(nickname)))
                .fetchOne();
        defaultMember에서 phoneNum username 동일한거 있는지 확인하고 처음부터 없으면 where에서 Null
        아니면 Expresssions.case인데
        defaultMember가 아니라 SiteMember나 OauthMember에서 각각 검출할 예정임
     */


}
