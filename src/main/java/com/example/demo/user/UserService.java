package com.example.demo.user;


import com.example.demo.role.RoleStatus;
import com.example.demo.authority.Authority;
import com.example.demo.board.BoardDslRepository;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.defaultuser.UserRepository;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.noneuser.NoneMemberRepository;
import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.oauthuser.OauthMemberRepository;
import com.example.demo.user.oauthuser.OauthUserDslRepository;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.siteuser.SiteMemberRepository;
import com.example.demo.userAuthority.UserAuthorityRepository;
import com.example.demo.entityjoin.JoinDslRepository;
import com.example.demo.userAuthority.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableCaching
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SiteMemberRepository siteMemberRepository;
    @Autowired
    OauthMemberRepository oauthMemberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    @Autowired
    OauthUserDslRepository oauthUserDslRepository;

    @Autowired
    UserJoinRepository userJoinRepository;
    @Autowired
    JoinDslRepository userAndBoardDslRepository;
    @Autowired
    BoardDslRepository boardJpaRepository;
    @Autowired
    NoneMemberRepository noneMemberRepository;

    // 키 넣을 때 parameter를 꼭 id에 맞춰넣어야됨
    // User 넣고 캐시 조사하는 이런건 안됨

    public DefaultMember createUser(DefaultMember user) {
        return userRepository.save(user);
    }
    // 이거 변경이 자주 안 일어나면 User 캐시로 달아야 하는데 달지는 않게 될듯
    @Transactional(readOnly = true)
    public DefaultMember findByUserIdEager(String userId) {
        return userJoinRepository.findBymember(userId);
    }
    public DefaultMember findUserByUserId(String userId) {
        System.out.println("findUserByUserId - userId = " + userId);
        DefaultMember user = userJoinRepository.findByDefaultMember(userId);
        System.out.println("user = " + user);
        // System.out.println("  ???sdf " );
        if(user != null) {
            System.out.println("user.getPassword() = " + user.getPassword());
            System.out.println("user = " + user);
            System.out.println("user.getUserAuthorities() = " + user.getUserAuthorities());
        }
        return user;
    }

    public void userAndUserAuthoritySave(DefaultMember defaultMember) {
        // 일,다수 넣고
        // 다수에 다 1개 집어넣던지
        defaultMember.getUserAuthorities().forEach(userAuthority -> System.out.println("userAuthority.getUserId() = " + userAuthority.getUserId()));
        defaultMember.getUserAuthorities().forEach(userAuthority -> userAuthority.setUserId(defaultMember));
        List<String> userAuthorityNames = defaultMember.getUserAuthorities().stream().map(UserAuthority::getAuthority).map(Authority::getAuthorityName)
                .collect(Collectors.toList());
        for(String authorityName : userAuthorityNames) {
            if(authorityName.equals(RoleStatus.ROLE_SITE_USER.name())) {
                siteMemberRepository.save((SiteMember) defaultMember);
                break;
            }
            if(authorityName.equals(RoleStatus.ROLE_OAUTH_USER.name())) {
                oauthMemberRepository.save((OauthMember) defaultMember);
                break;
            }
            if(authorityName.equals(RoleStatus.ROLE_ANONYMOUS.name())) {
                System.out.println("???dsfs");
                NoneMember noneMember = noneMemberRepository.save((NoneMember) defaultMember);
                System.out.println("save = " + noneMember.getUserId());
                System.out.println("noneMember.getPassword() = " + noneMember.getPassword());
                break;
            }
        }
        // defaultMemberRepository.save(userAuthority);
    }
    public Boolean isUserExist(String userId) {
        if(userRepository.findByUserId(userId) == null) return false;
        return true;
    }
    public List<MemberBoardQueryDTO> findBoards(long startBoardId,long boardQuantity) {
        List<MemberBoardQueryDTO> findBoards = userAndBoardDslRepository.findByUserIds(startBoardId,boardQuantity);
        return findBoards;
    }
    public DefaultMember saveUser(DefaultMember user) {
        return userRepository.save(user);
    }
    public SiteMember saveSiteMember(SiteMember user) {
        return siteMemberRepository.save(user);
    }

    public void updateUser(DefaultMember user) {
        System.out.println("Userservice.updateUser - user.getId()  = " + user.getId());
        System.out.println("Userservice.updateUser - user.getUserId() = " + user.getUserId());
        System.out.println("Userservice.updateUser - user.getPassword() = " + user.getPassword());
        userJoinRepository.saveMember(user.getId(),user.getUserId(),user.getPassword());
    }
    public boolean isAdmin(String userId) {
        if(userJoinRepository.getAdminCount(userId)>0) {
            System.out.println("isAdmin - success");
            return true;
        }
        System.out.println("isAdmin - fail");
        return false;
    }
    public String findPassword(String userId) {
        return userJoinRepository.findPassword(userId);
    }
    public boolean isExistenceUserIdOrEmailOrPhoneNumber(String userId, String email, String phoneNumber) {
        if(oauthUserDslRepository.getUserIdOrEmailOrPhoneNumber(userId,email,phoneNumber)>0) {
            return true;
        }
        return false;
    }
    public String findByPhoneNumberOrNickname(String phoneNumber,String nickname) {
        return userJoinRepository.findByPhoneNumberOrNickname(phoneNumber,nickname);
    }
    public String findUserIdByEmail(String email) {
        return userJoinRepository.findUserIdByEmail(email);
    }
    public void changeUserPasswordByEmailAndUserId(String changePassword,String email,String userId) {
        userJoinRepository.changeUserPasswordByEmailAndUserId(passwordEncoder.encode(changePassword),email,userId);
    }
}
