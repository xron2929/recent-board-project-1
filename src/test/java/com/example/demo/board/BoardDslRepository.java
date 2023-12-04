//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo.board;

import com.example.demo.config.TestConfig;
import com.example.demo.user.UserAuthorityAndUserIdDto;
import com.example.demo.user.defaultuser.QDefaultMember;
import com.example.demo.user.userAuthority.QUserAuthority;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
@Import({TestConfig.class})
public class BoardDslRepositoryTest {
    @Autowired
    EntityManager em;


    public UserAuthorityAndUserIdDto getAuthorityAndUserIdFromDB(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.em);

    }

    @ParameterizedTest
    @ValueSource(
            longs = {4L}
    )
    public void getAuthorityAndUserId(Long boardId) {
        UserAuthorityAndUserIdDto authorityAndUserId = this.getAuthorityAndUserIdFromDB(boardId);
        System.out.println("authorityAndUserId = " + authorityAndUserId);
    }
}
