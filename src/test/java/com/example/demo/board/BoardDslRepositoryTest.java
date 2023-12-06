//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo.board;

import com.example.demo.config.TestConfig;

import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.userAuthority.UserAuthority;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.board.QBoard.board;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.user.userAuthority.QUserAuthority.userAuthority;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
@Import({TestConfig.class})
public class BoardDslRepositoryTest {
    @PersistenceContext EntityManager em;
    @Autowired BoardService boardService;

    public UserAuthorityAndUserIdDto getAuthorityAndUserIdFromDBIsNotDuplication(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.em);
        return queryFactory
                .select(Projections.constructor(UserAuthorityAndUserIdDto.class,userAuthority.authority.authorityName,defaultMember.userId))
                .from(board)
                .leftJoin(board.member, defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .where(board.id.eq(boardId))
                .limit(1)
                .fetchOne();
    }


    public UserAuthorityAndUserIdDto getAuthorityAndUserIdFromDBIsDuplication(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.em);
        return queryFactory
                .select(Projections.constructor(UserAuthorityAndUserIdDto.class,userAuthority.authority.authorityName,defaultMember.userId))
                .from(board)
                .leftJoin(board.member, defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .where(board.id.loe(boardId))
                .fetchOne();
    }

    @ParameterizedTest
    @ValueSource(
            longs = {2L}
    )
    @Transactional
    @DisplayName("getAuthorityAndUserIdFromDBIsNotDuplication은 하나의 UserId 값만을 결과로 리턴한다.")
    public void getAuthorityAndUserIdFromDBIsNotDuplicationTest(Long boardId) {
        UserAuthorityAndUserIdDto authorityAndUserId = getAuthorityAndUserIdFromDBIsNotDuplication(boardId);
        System.out.println("authorityAndUserId = " + authorityAndUserId);
    }
    @ParameterizedTest
    @ValueSource(
            longs = {2L}
    )
    @Transactional
    @DisplayName("DB의 쿼리 결과가 하나보다 큰 값을 요청할 경우, NonUniqueResultException 발생")
    public void getAuthorityAndUserIdFromDBIsDuplicationTest(Long boardId) {
        Exception dbDuplicateException = assertThrows(NonUniqueResultException.class, () ->
                getAuthorityAndUserIdFromDBIsDuplication(boardId));
    }



}
