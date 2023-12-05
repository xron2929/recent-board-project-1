package com.example.demo.user;


import com.example.demo.boradAndUser.MemberBoardQueryDTO;
import com.example.demo.boradAndUser.UserJoinRepository;
import com.example.demo.role.RoleStatus;
import com.example.demo.user.authority.Authority;
import com.example.demo.board.BoardDslRepository;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.defaultuser.UserRepository;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.noneuser.NoneMemberRepository;
import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.oauthuser.OauthMemberRepository;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.siteuser.SiteMemberRepository;
import com.example.demo.user.userAuthority.UserAuthorityRepository;
import com.example.demo.user.userAuthority.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableCaching
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserJoinRepository userJoinRepository;
    @Autowired
    SiteMemberRepository siteMemberRepository;
    @Autowired
    OauthMemberRepository oauthMemberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    @Autowired
    UserDslRepository userDslRepository;

    @Autowired
    BoardDslRepository boardJpaRepository;
    @Autowired
    NoneMemberRepository noneMemberRepository;

    // 키 넣을 때 parameter를 꼭 id에 맞춰넣어야됨
    // User 넣고 캐시 조사하는 이런건 안됨

    public DefaultMember createUser(DefaultMember user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public DefaultMember findByUserIdEager(String userId) {
        return userDslRepository.findBymember(userId);
    }
    public DefaultMember findUserByUserId(String userId) {
        System.out.println("findUserByUserId - userId = " + userId);
        DefaultMember user = userDslRepository.findByDefaultMember(userId);
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

        defaultMember.getUserAuthorities().forEach(userAuthority -> userAuthority.setUserId(defaultMember));
        List<String> userAuthorityNames = defaultMember.getUserAuthorities().stream().map(UserAuthority::getAuthority).map(Authority::getAuthorityName)
                .collect(Collectors.toList());
        defaultMember.getUserAuthorities().forEach(userAuthority -> System.out.println("userAuthority.getUserId() = " + userAuthority.getUserId()));
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
    public void deleteSiteUser(SiteMember siteMember) {
        siteMemberRepository.delete(siteMember);
    }
    public List<MemberBoardQueryDTO> findBoards(long startBoardId, long boardQuantity) {
         List<MemberBoardQueryDTO> findBoards = userJoinRepository.findByUserIds(startBoardId,boardQuantity);
        return findBoards;
    }
    public NoneMember saveNoneUser(NoneMember user) {
        return noneMemberRepository.save(user);
    }
    public SiteMember saveSiteUser(SiteMember user) throws RuntimeException{
        SiteMember siteMember = siteMemberRepository.findByUserId(user.getUserId()).orElse(null);
        System.out.println("siteMember = " + siteMember);
        if(siteMember == null) {
            return siteMemberRepository.save(user);
        }
        throw new RuntimeException("사이트 또는 어드민 회원은 동일한 userId를 가진 테이블을 삽입할 수 없습니다.");

    }

    public void updateUser(DefaultMember user) {
        System.out.println("Userservice.updateUser - user.getId()  = " + user.getId());
        System.out.println("Userservice.updateUser - user.getUserId() = " + user.getUserId());
        System.out.println("Userservice.updateUser - user.getPassword() = " + user.getPassword());
        userDslRepository.saveMember(user.getId(),user.getUserId(),user.getPassword());
    }
    public boolean isAdmin(String userId) {
        if(userDslRepository.getAdminCount(userId)>0) {
            System.out.println("isAdmin - success");
            return true;
        }
        System.out.println("isAdmin - fail");
        return false;
    }
    public String findPassword(String userId) {
        return userDslRepository.findPassword(userId);
    }
    public String findByPhoneNumberOrNickname(String phoneNumber,String nickname) {
        List<String> duplicatePhoneNumberAndNicknameResults = userDslRepository.findByPhoneNumberOrNickname(phoneNumber, nickname);

        for (String duplicatePhoneNumberAndNicknameResult:duplicatePhoneNumberAndNicknameResults) {
            if (!duplicatePhoneNumberAndNicknameResult.equals(null) && !duplicatePhoneNumberAndNicknameResult.isBlank()) {
                System.out.println("duplicatePhoneNumberAndNicknameResult = " + duplicatePhoneNumberAndNicknameResult);
                return duplicatePhoneNumberAndNicknameResult;
            }
        }
        return null;
    }
    public String findUserIdByEmail(String email) {
        return userDslRepository.findUserIdByEmail(email);
    }
    public void changeUserPasswordByEmailAndUserId(String changePassword,String email,String userId) {
        userDslRepository.changeUserPasswordByEmailAndUserId(passwordEncoder.encode(changePassword),email,userId);
    }
    public SiteMember findBySiteMemberId(String memberId) {
        return siteMemberRepository.findByUserId(memberId).orElse(null);
    }
}
