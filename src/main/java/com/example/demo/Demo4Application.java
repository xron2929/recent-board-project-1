package com.example.demo;

import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import com.example.demo.alarm.Alarm;
import com.example.demo.alarm.AlarmService;
import com.example.demo.authority.Authority;
import com.example.demo.authority.AuthorityRepository;
import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.cookie.CookieManager;
import com.example.demo.gender.Gender;
import com.example.demo.image.AmazonS3Config;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.SecurityConfig;
import com.example.demo.security.authentication.AuthenticationConfig;
import com.example.demo.security.jwt.Trans;
import com.example.demo.user.UserService;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.noneuser.NoneMemberRepository;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.userAuthority.UserAuthority;
import com.example.demo.userAuthority.UserAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@Import({SecurityConfig.class, AuthenticationConfig.class, AmazonS3Config.class})
public class Demo4Application {
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    NoneMemberRepository noneMemberRepository;
    public static void main(String[] args) {
        SpringApplication. run(Demo4Application.class, args);
    }
    @Bean
    public AuditorAware<String> auditorProvider() {
        return ()-> Optional.of(UUID.randomUUID().toString());
    }
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CookieManager cookieManager;
    @Autowired
    AlarmService alarmService;
    @Autowired
    AdditionalInformationRedisTemplate redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    void saveAdmin() {
        Authority authority = new Authority(RoleStatus.ROLE_ANONYMOUS.name());
        Authority authority2 = new Authority(RoleStatus.ROLE_ADMIN.name());
        Authority authority3 = new Authority(RoleStatus.ROLE_SITE_USER.name());
        Authority authority4 = new Authority(RoleStatus.ROLE_OAUTH_USER.name());

        UserAuthority userAuthority = new UserAuthority(authority);
        UserAuthority userAuthority2 = new UserAuthority(authority2);
        UserAuthority userAuthority3 = new UserAuthority(authority3);
        UserAuthority userAuthority4 = new UserAuthority(authority2);
        UserAuthority userAuthority5 = new UserAuthority(authority3);
        authorityRepository.save(authority);
        authorityRepository.save(authority2);
        authorityRepository.save(authority3);
        authorityRepository.save(authority4);
        List<UserAuthority> userAuthorities = new ArrayList<>();
        List<UserAuthority> userAuthorities2 = new ArrayList<>();
        List<UserAuthority> userAuthorities3 = new ArrayList<>();
        userAuthorities.add(userAuthority);
        userAuthorities2.add(userAuthority2);
        userAuthorities2.add(userAuthority3);
        userAuthorities3.add(userAuthority4);
        userAuthorities3.add(userAuthority5);

        NoneMember noneMember = NoneMember
                .builder()
                .userId("234324")
                .password("fdsfs")
                .ip("1.2.3.4")
                .userAuthorities(userAuthorities)
                .build();
        userService.userAndUserAuthoritySave(noneMember);
        // userAuthorities.add(userAuthority);
        SiteMember siteMember = SiteMember
                .builder()
                .userId("123423")
                .password("fdssfs")
                .gender(new Gender(Trans.UNSELECTED))
                .nickname("dsfw3")
                .age(21)
                .userAuthorities(userAuthorities2)
                .build();
        // noneMemberRepository.save(noneMember2);
        userService.userAndUserAuthoritySave(siteMember);
        Board board = new Board("dfdfa","dsfadfads",noneMember,false);
        Board board2 = new Board("dfdfa","dsfadfads",siteMember,false);
        boardService.saveBoard(board);
        boardService.saveBoard(board2);
        userAuthorities2.forEach(userAuthority1 -> System.out.println("userAuthority1.getUserId() = " + userAuthority1.getUserId()));
        String password = passwordEncoder.encode("12345");
        SiteMember siteMember2 = SiteMember
                .builder()
                .userId("12345")
                .password(password)
                .nickname("dsfw3")
                .gender(new Gender(Trans.UNSELECTED))
                .age(21)
                .userAuthorities(userAuthorities3)
                .build();
        userService.userAndUserAuthoritySave(siteMember2);
        List<String> sdf =new ArrayList<>();
        sdf.add("Sdfds");


        Map<String,List<Alarm>> byUserId = alarmService.findALLAlarms();
        System.out.println("byUserId.size() = " + byUserId.size());
        if(byUserId.size()!=0) {
            Set<String> keySet = byUserId.keySet();
            for (String key:keySet){
                redisTemplate.setAllAlarms(key,byUserId.get(key));
            }
        }

    }

}
