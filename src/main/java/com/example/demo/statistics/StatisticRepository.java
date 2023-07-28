package com.example.demo.statistics;

import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.Trans;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.authority.QAuthority.authority;
import static com.example.demo.board.QBoard.board;
import static com.example.demo.user.defaultuser.QDefaultMember.defaultMember;
import static com.example.demo.userAuthority.QUserAuthority.userAuthority;

@Repository
public class StatisticRepository {
    @PersistenceContext EntityManager em;

    public List<Object[]> getTransOauthDslData() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        String query = "SELECT " +
                "g.trans,count(g.trans) from gender g " +
                "left join oauth_member om on g.id = om.gender_id " +
                "left join site_member sm on g.id = sm.gender_id " +
                "group by g.trans;";
        return em.createNativeQuery(query).getResultList();
    }

        public StatisticTransDto getTransOauthData() {
        List<Object[]>results = getTransOauthDslData();

        Long maleCount = 0L;
        Long femaleCount = 0L;
        Long unSelectedCount = 0L;
        for (Object[] result : results) {
            String transData = (String) result[0];
            BigInteger count = (BigInteger) result[1];
            System.out.println("count = " + count);
            System.out.println("transData = " + transData);
            if (transData.equals(Trans.MALE.name())) {
                maleCount =  maleCount + count.longValue();
            } else if (transData.equals(Trans.FEMALE.name())) {
                femaleCount = femaleCount + count.longValue();
            } else if (transData.equals(Trans.UNSELECTED.name())) {
                unSelectedCount = unSelectedCount + count.longValue();
            }
        }
        Long totalTransCount = maleCount+femaleCount+unSelectedCount;
        System.out.println("unSelectedCount = " + unSelectedCount);
        System.out.println("maleCount = " + maleCount);
        System.out.println("femaleCount = " + femaleCount);
        System.out.println("totalTransCount = " + totalTransCount);
        return new StatisticTransDto(unSelectedCount,maleCount,femaleCount,totalTransCount);
    }

    public StatisticBoardAuthorityDto getAuthorityData() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> results = queryFactory.select(authority.authorityName,authority.count())
                .from(board)
                .innerJoin(board.member, defaultMember)
                .innerJoin(defaultMember.userAuthorities, userAuthority)
                .innerJoin(userAuthority.authority, authority)
                .where(authority.authorityName.in(RoleStatus.ROLE_SITE_USER.name(),
                                RoleStatus.ROLE_OAUTH_USER.name(),RoleStatus.ROLE_ANONYMOUS.name()))
                .groupBy(authority.authorityName)
                .fetch();


        Long siteUserCount = 0L;
        Long oauthUserCount = 0L;
        Long noneUserCount = 0L;

        for (Tuple result : results) {
            String boardAuthority = result.get(authority.authorityName);
            Long count = result.get(authority.count());
            if (boardAuthority.equals(RoleStatus.ROLE_OAUTH_USER.name())) {
                oauthUserCount = count;
            } else if (boardAuthority.equals(RoleStatus.ROLE_SITE_USER.name())) {
                siteUserCount = count;
            } else if (boardAuthority.equals(RoleStatus.ROLE_ANONYMOUS.name())) {
                noneUserCount = count;
            }
        }
        Long totalAuthorityCount = oauthUserCount+siteUserCount+noneUserCount;
        // male: 10 total : 20
        // maleCount * 100  * 100 / totalCount / 100 = 50
        System.out.println("oauthUserCount = " + oauthUserCount);
        System.out.println("siteUserCount = " + siteUserCount);
        System.out.println("noneUserCount = " + noneUserCount);
        System.out.println("totalAuthorityCount = " + totalAuthorityCount);

        return new StatisticBoardAuthorityDto(oauthUserCount,siteUserCount,noneUserCount,totalAuthorityCount);
    }


    public List<StatisticBoardWriteRepositoryDto> getPeriodicBoardWritingCount(LocalDateTime startTime, LocalDateTime endTime) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        ArrayList<String> readUserAccountKind = new ArrayList<String>();

        readUserAccountKind.add(RoleStatus.ROLE_OAUTH_USER.name());
        readUserAccountKind.add(RoleStatus.ROLE_SITE_USER.name());

        readUserAccountKind.add(RoleStatus.ROLE_ANONYMOUS.name());
        String query = "SELECT a.authority_name, COUNT(*), b.created_date " +
                "FROM board b " +
                "INNER JOIN default_member m ON b.member_id = m.id " +
                "INNER JOIN user_authority ua ON m.id = ua.user_id " +
                "INNER JOIN authority a ON ua.authority_name = a.authority_name " +
                "WHERE b.created_date BETWEEN :startTime AND :endTime AND a.authority_name IN (:names)" +
                "GROUP BY a.authority_name, TIMESTAMPDIFF(MINUTE, b.created_date, CURRENT_TIMESTAMP)";
        List<Object[]> results  = em.createNativeQuery(query)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("names", readUserAccountKind)
                .getResultList();

        List<StatisticBoardWriteRepositoryDto> dtos = new ArrayList<>();

        for (Object[] result : results) {
            StatisticBoardWriteRepositoryDto dto = new StatisticBoardWriteRepositoryDto((String) result[0], (BigInteger) result[1], (Timestamp) result[2]);
            dtos.add(dto);
        }

        return dtos;
    }


}
