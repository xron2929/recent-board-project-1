package com.example.demo.util;

import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.board.UserAuthorityAndUserIdDto;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.*;
import com.example.demo.user.IsAllowedAuthorityStatus;
import com.example.demo.user.UserIdAndIsAllowedAuthorityStatus;
import com.example.demo.user.UserService;
import com.example.demo.user.authority.Authority;
import com.example.demo.user.gender.Gender;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.userAuthority.UserAuthority;
import com.example.demo.util.cookie.CookieManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
public class UserManagerTest {

    @Autowired
    UserManager userManager;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    TransManager transManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    @DisplayName("사이트 계정으로 회원가입한 후, db에 있는 계정과 동일한 아이디와 비밀번호를 가졌으면 ValidationStatus.USER_ACCOUNT 반환")
    void successValidateSiteOrAdminPasswordTest(String userId, String password) throws JsonProcessingException {
        // given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(userId)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(password).build();
        joinSiteUser(userRequestDto);
        String dbPassword = userService.findPassword(userId);

        // when
        UserIdAndValidationDtoAndAccessToken userIdAndValidateUser = userManager.validateSiteOrAdminPassword
                (userRequestDto.getUserId(), userRequestDto.getPassword(), dbPassword);
        // then
        Assertions.assertThat(userIdAndValidateUser.getValidationStatus()).isEqualTo(ValidationStatus.USER_ACCOUNT);


    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    @DisplayName("사이트 계정으로 회원가입한 후, db에 있는 계정과 동일한 아이디와 다른 비밀번호를 가졌으면 ValidationStatus.ERROR_ACCOUNT 반환")
    public void failureValidateSiteOrAdminPasswordTestOtherPassword(String userId, String password) {
        // given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(userId)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(password).build();
        joinSiteUser(userRequestDto);
        String dbPassword = userService.findPassword(userId);

        // when
        UserIdAndValidationDtoAndAccessToken userIdAndValidateUser = userManager.validateSiteOrAdminPassword
                (userRequestDto.getUserId(), "otherPassword", dbPassword);
        // then
        Assertions.assertThat(userIdAndValidateUser.getValidationStatus()).isEqualTo(ValidationStatus.ERROR_ACCOUNT);
        // String accessToken = jwtManager.setAccessToken(userRequestDto);
    }

    private void joinSiteUser(UserRequestDto userRequestDto1) {

        Authority authority = new Authority("ROLE_SITE_USER");
        UserAuthority userAuthority = new UserAuthority(authority);

        Trans trans1 = transManager.getTrans(userRequestDto1.getTrans());
        SiteMember siteUser1 = setSiteUser(trans1, userRequestDto1, userAuthority);

        SiteMember findUserByUserId1 = userService.findBySiteMemberId(userRequestDto1.getUserId());

        if (findUserByUserId1 != null) {
            userService.deleteSiteUser(findUserByUserId1);
        }
        userService.saveSiteUser(siteUser1);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    @DisplayName("사이트 계정으로 회원가입한 후, db에 있는 계정과 다른 아이디와 동일한 비밀번호를 가졌으면 ValidationStatus.ERROR_ACCOUNT 반환")
    public void failureValidateSiteOrAdminPasswordTestOtherUserId(String userId, String password) {
        // given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(userId)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(password).build();
        joinSiteUser(userRequestDto);
        String dbPassword = userService.findPassword("otherPassword");

        // when
        UserIdAndValidationDtoAndAccessToken userIdAndValidateUser = userManager.validateSiteOrAdminPassword
                (userRequestDto.getUserId(), userRequestDto.getPassword(), dbPassword);
        // then
        Assertions.assertThat(userIdAndValidateUser.getValidationStatus()).isEqualTo(ValidationStatus.ERROR_ACCOUNT);

    }

    private SiteMember setSiteUser(Trans trans, UserRequestDto userRequestDto, UserAuthority userAuthority) {
        Gender gender = new Gender(trans);
        SiteMember user = UserRequestDto.from(userRequestDto, trans);
        user.setGender(gender);
        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(userAuthority);
        user.setUserAuthorities(userAuthorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    @DisplayName("사이트 계정으로 회원가입한 후, db에 있는 계정과 다른 아이디와 동일한 비밀번호를 가졌으면 ValidationStatus.ERROR_ACCOUNT 반환")
    UserIdAndIsAllowedAuthorityStatus checkUserIdAndValidationTest(String userId,String password) throws JsonProcessingException {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(userId)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(password).build();
        joinSiteUser(userRequestDto);
        String dbPassword = userService.findPassword("otherPassword");
        String accessToken = jwtManager.setAccessToken(userRequestDto);
        // board를 비회원이 작성 후 비회원이랑 비교
        // board를 비회원이 작성 후 회원이랑 비교
        // board를 비회원이 작성 후 어드민이랑 비교
        // board를 회원이 작성 후 회원이랑 비교
        // board를 회원이 작성 후 어드민이랑 비교
        // board를 어드민이 작성후 비회원이랑 비교
        // Board를  어드민이 작성후 회원이랑 비교
        // board를 어드민이 작성 후 어드민이랑 비교
        // 각자 동일권한이라도 성공케이스랑 비교
        Board board = new Board();
        boardService.saveBoard(board);
        UserAuthorityAndUserIdDto userAuthorityAndUserIdDto = boardService.getAuthorityAndUserId(boardId);
        TokenStatus tokenValidation = jwtManager.validation(accessToken);
        String boardReadAuthority = jwtManager.getAuthorityName(accessToken);
        UserRequestDto boardReadUserRequestDto = jwtManager.getUserRequestDto(accessToken);
        if(boardReadAuthority.equals(RoleStatus.ROLE_ADMIN.name())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.HIGHER_READER_ACCOUNT);
        }
        if(boardReadUserRequestDto.getUserId().equals(userAuthorityAndUserIdDto.getUserId()) &&
                boardReadAuthority.equals(userAuthorityAndUserIdDto.getUserAuthorityName())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.SAME_USER_ACCOUNT);
        }
        return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT);
    }
    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of("ab", "1234"),
                Arguments.of("aa", "12345"),
                Arguments.of("ac", "12346")
        );
    }


}
