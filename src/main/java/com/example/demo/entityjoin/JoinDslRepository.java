package com.example.demo.entityjoin;

import com.example.demo.board.QBoard;
import com.example.demo.user.MemberBoardQueryDTO;
import com.example.demo.user.UserIdAndPasswordDto;
import com.example.demo.user.defaultuser.QDefaultMember;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.demo.board.QBoard.board;
import static com.example.demo.comment.QParentComment.parentComment;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.user.noneuser.QNoneMember.noneMember;
import static com.example.demo.userAuthority.QUserAuthority.userAuthority;
import static com.querydsl.core.types.dsl.Expressions.constant;

/*
UserAndBoard 이런식으로 잡아서 리팩토링 하는 아키텍처인 경우:
user랑 board 같이 아키텍처가 user,authority,board 가 세세하게 안 나눠진 경우 + 유지보수가 큰 틀 안에서 벗어나지 않는 경우는
이거 꼼꼼하게 하면 되는데 나는 코드가 여기서 더 확장 가능성이 있어서
그냥 JoinRepository안에 집어 넣고, 여기서 더 확장할 생각임
user가 userAuthority를 가지는 것도 패키지 구조상 되는 부분이 있으니(data-jpa save 함수 생각)
authority랑 board join하는 거 있으면 그 부분만 따로 join 패키지 안에 dslRepository를 넣는 것도 괜찮지만
업데이트 할 때 상당히 귀찮은 짓이라 이렇게 해야할듯
insert는 해당 컬럼의 보조컬럼들까지 업데이트 하는 거라 생각해 제외하고, 조회 커리는 JOIN에 다른 컬럼 여부 있을 시 여기 들어감
 */
@Repository
public class JoinDslRepository {
    @PersistenceContext
    private EntityManager em;
    public List<MemberBoardQueryDTO>findByUserIds(Long startId,Long boardQuantity) {
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
    public BoardFindDataDto findEditBoard(Long boardId) {
        System.out.println("boardId = " + boardId);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .select(Projections.constructor(BoardFindDataDto.class,
                        board.id, board.title, board.content,
                        board.member.userId))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.id.eq(boardId))
                .fetchOne();
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

    public UserIdAndPasswordDto findUserIdAndPassword(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(
                        Projections.constructor(UserIdAndPasswordDto.class, defaultMember.userId, defaultMember.password))
                .from(board)
                .innerJoin(board.member, defaultMember)
                .where(board.id.eq(boardId)).fetchOne();
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
                        noneMember.userId
                        ,noneMember.password))
                .from(board)
                .leftJoin(noneMember).on(board.member.id.eq(noneMember.id))
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

    public List<CommentReadDataDto> findByIdsSubQuery(List<Long> commentId) {
        commentId.forEach(comment-> System.out.println("comment = " + comment));
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(Projections.constructor(CommentReadDataDto.class,
                        parentComment.member.nickname,parentComment.content,parentComment.member.nickname))
                .from(parentComment)
                .leftJoin(parentComment.member, QDefaultMember.defaultMember)
                .where(parentComment.id.in(commentId))
                .fetch();
    }
    public List<CommentReadDataDto> readTenParentComment2(Long startId,Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(Projections.constructor(CommentReadDataDto.class,
                        parentComment.member.userId,parentComment.content))
                .from(parentComment)
                .leftJoin(parentComment.board, QBoard.board)
                .leftJoin(parentComment.member, QDefaultMember.defaultMember)
                .limit(boardId)
                .offset(startId)
                .fetch();

    }
    public List<CommentReadDataDto> readTenParentComment(Long startId, Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(Projections.constructor(CommentReadDataDto.class,
                        parentComment.member.userId,parentComment.content))
                .from(parentComment)
                .leftJoin(parentComment.board, QBoard.board)
                .leftJoin(parentComment.member, QDefaultMember.defaultMember)
                .where(parentComment.id.goe(startId)
                        .and(parentComment.id.lt(startId+11)))
                .fetch();

    }

}
