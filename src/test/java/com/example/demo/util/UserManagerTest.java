package com.example.demo.util;

import com.example.demo.board.*;
import com.example.demo.board.image.ImageService;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.*;
import com.example.demo.user.IsAllowedAuthorityStatus;
import com.example.demo.user.UserIdAndIsAllowedAuthorityStatus;
import com.example.demo.user.UserService;
import com.example.demo.user.authority.Authority;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.gender.Gender;
import com.example.demo.user.noneuser.NoneMember;
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
import java.util.List;
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
    EditBoardMapper editBoardMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;
    @Autowired
    ImageService imageService;
    @ParameterizedTest
    @MethodSource("provideSuccessValidateSiteOrAdminPasswordTest")
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
    @MethodSource("provideFailureValidateSiteOrAdminPasswordTestOtherPassword")
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

    private List<UserAuthority> joinSiteUser(UserRequestDto userRequestDto1) {

        Authority authority = new Authority("ROLE_SITE_USER");
        UserAuthority userAuthority = new UserAuthority(authority);
        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        Trans trans1 = transManager.getTrans(userRequestDto1.getTrans());

        userAuthorities.add(userAuthority);
        SiteMember siteUser1 = setUser(trans1, userRequestDto1, userAuthorities);

        SiteMember findUserByUserId1 = userService.findBySiteMemberId(userRequestDto1.getUserId());
        System.out.println("findUserByUserId1 = " + findUserByUserId1);

        if (findUserByUserId1 != null) {

            userService.deleteSiteUser(findUserByUserId1);
        }

        userService.userAndUserAuthoritySave(siteUser1);
        return userAuthorities;
    }



    @ParameterizedTest
    @MethodSource("provideUserIdAndPassword")
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


    @ParameterizedTest
    @MethodSource("provideHigherReaderCheckUserIdAndValidationTest")
    @DisplayName("사이트 계정으로 회원가입한 후, 게시글 작성 후 checkUserIdAndValid를ation에 아까 게시글을 작성한 유저를 인자로 넣어서 SAME_USER를 확인")
    void HigherReaderCheckUserIdAndValidationTest(String userId,String password) throws JsonProcessingException {
        Authority authority1 = new Authority(RoleStatus.ROLE_SITE_USER.name());
        UserAuthority userAuthority1 = new UserAuthority(authority1);

        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(userAuthority1);
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(userId)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .phoneNumber("010-8434-0311")
                .trans("남성")
                .userAuthorities(userAuthorities)
                .password(password).build();
        joinSiteUser(userRequestDto);
        String accessToken = jwtManager.setAccessToken(userRequestDto);

        SiteMember findUserByUserId1 = userService.findBySiteMemberId(userRequestDto.getUserId());

        System.out.println("user.getId() = " + findUserByUserId1.getId());
        Long finalBoardId = boardService.getFinalBoardId();
        BoardEditUserDto boardEditUserDto = BoardEditUserDto
                .builder()
                .id(finalBoardId)
                .isSecret(false)
                .likeCount("0")
                .disLikeCount("0")
                .title("테스트 board 추가")
                .username(findUserByUserId1.getNickname())
                .build();
        Board board = new Board(boardEditUserDto.getId(),boardEditUserDto.getTitle(), boardEditUserDto.getContent(),
                findUserByUserId1,boardEditUserDto.isSecret());
        Board board1 = boardService.saveBoard(board);
        UserAuthorityAndUserIdDto userAuthorityAndUserIdDto = boardService.getAuthorityAndUserId(board1.getId());

        String boardReadAuthority = jwtManager.getAuthorityName(accessToken);
        UserRequestDto boardReadUserRequestDto = jwtManager.getUserRequestDto(accessToken);
        UserIdAndIsAllowedAuthorityStatus userIdAndIsAllowedAuthorityStatus = checkData(boardReadAuthority, boardReadUserRequestDto, userAuthorityAndUserIdDto);

        Assertions.assertThat(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus()).isEqualTo(IsAllowedAuthorityStatus.SAME_USER_ACCOUNT);
        imageService.deleteBoard(finalBoardId);
    }
    @ParameterizedTest
    @MethodSource("provideUnSameUserCheckUserIdAndValidationTest")
    @DisplayName("사이트 계정으로 회원가입한 후, 게시글 작성 후 checkUserIdAndValidation에 " +
            "아까 게시글을 작성한 유저와 동등한 권한을 가진 다른 유저를 인자로 넣어서 UN_SAME_USER 확인")
    void unSameUserCheckUserIdAndValidationTest(String writerUserId,String writerUserPassword,String readerUserId,String readerUserPassword) throws JsonProcessingException {
    Authority authority1 = new Authority(RoleStatus.ROLE_ANONYMOUS.name());
        UserAuthority userAuthority1 = new UserAuthority(authority1);

        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(userAuthority1);

        UserRequestDto readerUserRequestDto = UserRequestDto.builder()
                .userId(readerUserId)
                .userAuthorities(userAuthorities)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(readerUserPassword).build();
        joinNoneUser(readerUserRequestDto,userAuthorities);
        String otherUserAccessToken = jwtManager.setAccessToken(readerUserRequestDto);

        Authority authority2 = new Authority("ROLE_SITE_USER");
        UserAuthority userAuthority2 = new UserAuthority(authority2);
        ArrayList<UserAuthority> userAuthorities2 = new ArrayList<>();
        UserRequestDto writerUserRequestDto = UserRequestDto
                .builder()
                .userId(writerUserId)
                .password(writerUserPassword)
                .age(20l)
                .userAuthorities(userAuthorities2)
                .email("asdf@gmail.com")
                .nickname("asdfwewaef")
                .phoneNumber("010-8434-0361")
                .trans("남성")
                .build();
        joinSiteUser(writerUserRequestDto);
        SiteMember findUserByUserId1 = userService.findBySiteMemberId(writerUserRequestDto.getUserId());
        System.out.println("findUserByUserId1 = " + findUserByUserId1);
        Long finalBoardId = boardService.getFinalBoardId();

        BoardEditUserDto boardEditUserDto = BoardEditUserDto
                .builder()
                .id(finalBoardId)
                .isSecret(false)
                .likeCount("0")
                .disLikeCount("0")
                .title("테스트 board 추가")
                .username(findUserByUserId1.getNickname())
                .build();
        Board board = new Board(boardEditUserDto.getId(),boardEditUserDto.getTitle(), boardEditUserDto.getContent(),
                findUserByUserId1,boardEditUserDto.isSecret());
        Board board1 = boardService.saveBoard(board);
        System.out.println("board1.getId() = " + board1.getId());
        UserAuthorityAndUserIdDto boardWriterUserAuthorityAndUserIdDto = boardService.getAuthorityAndUserId(board1.getId());
        System.out.println("boardWriterUserAuthorityAndUserIdDto = " + boardWriterUserAuthorityAndUserIdDto);

        String boardReaderAuthority = jwtManager.getAuthorityName(otherUserAccessToken);
        UserRequestDto boardReaderUserRequestDto = jwtManager.getUserRequestDto(otherUserAccessToken);

        UserIdAndIsAllowedAuthorityStatus userIdAndIsAllowedAuthorityStatus = checkData(boardReaderAuthority, boardReaderUserRequestDto, boardWriterUserAuthorityAndUserIdDto);
        Assertions.assertThat(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus()).isEqualTo(IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT);
        imageService.deleteBoard(finalBoardId);
    }
    @ParameterizedTest
    @MethodSource("provideSameUserCheckUserIdAndValidationTest")
    @DisplayName("사이트 계정으로 회원가입한 후, 게시글 작성 후 checkUserIdAndValidation에 " +
            "아까 게시글을 작성한 유저보다 높은 권한을 가진 어드민 유저를 인자로 넣어서 HIGHER_READER_ACCOUNT 확인")
    void sameUserCheckUserIdAndValidationTest(String writerUserId,String writerUserPassword,String readerUserId,String readerUserPassword) throws JsonProcessingException {

        Authority authority1 = new Authority(RoleStatus.ROLE_ADMIN.name());
        UserAuthority userAuthority1 = new UserAuthority(authority1);
        Authority authority2 = new Authority(RoleStatus.ROLE_SITE_USER.name());
        UserAuthority userAuthority2 = new UserAuthority(authority2);
        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(userAuthority1);
        userAuthorities.add(userAuthority2);
        UserRequestDto readerUserRequestDto = UserRequestDto.builder()
                .userId(readerUserId)
                .userAuthorities(userAuthorities)
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password(readerUserPassword).build();
        joinAdminUser(readerUserRequestDto,userAuthorities);
        String otherUserAccessToken = jwtManager.setAccessToken(readerUserRequestDto);

        DefaultMember userByUserId = userService.findUserByUserId(writerUserId);
        UserRequestDto writerUserRequestDto = UserRequestDto
                .builder()
                .userId(writerUserId)
                .password(writerUserPassword)
                .age(20l)
                .email("asdf@gmail.com")
                .nickname("asdfwewaef")
                .phoneNumber("010-8434-0361")
                .trans("남성")
                .build();
        joinSiteUser(writerUserRequestDto);
        SiteMember findUserByUserId1 = userService.findBySiteMemberId(writerUserRequestDto.getUserId());
        Long finalBoardId = boardService.getFinalBoardId();
        BoardEditUserDto boardEditUserDto = BoardEditUserDto
                .builder()
                .id(finalBoardId)
                .isSecret(false)
                .likeCount("0")
                .disLikeCount("0")
                .title("테스트 board 추가")
                .username(findUserByUserId1.getNickname())
                .build();
        Board board = new Board(boardEditUserDto.getId(),boardEditUserDto.getTitle(), boardEditUserDto.getContent(),
                findUserByUserId1,boardEditUserDto.isSecret());
        Board board1 = boardService.saveBoard(board);
        UserAuthorityAndUserIdDto boardWriterUserAuthorityAndUserIdDto = boardService.getAuthorityAndUserId(board1.getId());

        String boardReaderAuthority = jwtManager.getAuthorityName(otherUserAccessToken);
        UserRequestDto boardReaderUserRequestDto = jwtManager.getUserRequestDto(otherUserAccessToken);

        UserIdAndIsAllowedAuthorityStatus userIdAndIsAllowedAuthorityStatus = checkData(boardReaderAuthority, boardReaderUserRequestDto, boardWriterUserAuthorityAndUserIdDto);
        Assertions.assertThat(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus()).isEqualTo(IsAllowedAuthorityStatus.HIGHER_READER_ACCOUNT);
        imageService.deleteBoard(finalBoardId);
    }
    private List<UserAuthority> joinAdminUser(UserRequestDto userRequestDto1,ArrayList<UserAuthority>userAuthorities) {

        Trans trans1 = transManager.getTrans(userRequestDto1.getTrans());
        SiteMember siteUser1 = setUser(trans1, userRequestDto1, userAuthorities);
        SiteMember findUserByUserId1 = userService.findBySiteMemberId(userRequestDto1.getUserId());
        if (findUserByUserId1 != null) {
            userService.deleteSiteUser(findUserByUserId1);
        }
        userService.saveSiteUser(siteUser1);
        return userAuthorities;
    }
    private List<UserAuthority> joinNoneUser(UserRequestDto userRequestDto1,ArrayList<UserAuthority>userAuthorities) {

        Trans trans1 = transManager.getTrans(userRequestDto1.getTrans());
        NoneMember findUserByUserId1 = userService.findByNoneMemberId(userRequestDto1.getUserId());
        if (findUserByUserId1 != null) {
            userService.deleteNoneUser(findUserByUserId1);
        }
        NoneMember noneMember = NoneMember
                .builder()
                .userId(userRequestDto1.getUserId())
                .userAuthorities(userAuthorities)
                .password(userRequestDto1.getPassword())
                .build();
        userService.userAndUserAuthoritySave(noneMember);
        return userAuthorities;
    }
    private SiteMember setUser(Trans trans, UserRequestDto userRequestDto, ArrayList<UserAuthority> userAuthorities) {
        Gender gender = new Gender(trans);
        SiteMember user = UserRequestDto.from(userRequestDto, trans);
        user.setGender(gender);
        user.setUserAuthorities(userAuthorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }


    public UserIdAndIsAllowedAuthorityStatus checkData(String boardReadAuthority, UserRequestDto boardReadUserRequestDto,UserAuthorityAndUserIdDto userAuthorityAndUserIdDto) {
        if(boardReadAuthority.equals(RoleStatus.ROLE_ADMIN.name())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.HIGHER_READER_ACCOUNT);
        }
        if(boardReadUserRequestDto.getUserId().equals(userAuthorityAndUserIdDto.getUserId()) &&
                boardReadAuthority.equals(userAuthorityAndUserIdDto.getUserAuthorityName())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.SAME_USER_ACCOUNT);
        }
        return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT);
    }

    private static Stream<Arguments> provideSuccessValidateSiteOrAdminPasswordTest() {
        return Stream.of(
                Arguments.of("asdfawe", "1234"),
                Arguments.of("aaafeweaw", "12345"),
                Arguments.of("acafwefmwe", "12346")
        );
    }
    private static Stream<Arguments> provideFailureValidateSiteOrAdminPasswordTestOtherPassword() {
        return Stream.of(
                Arguments.of("abgwawmlw", "1234"),
                Arguments.of("aaafwnknkwfe", "12345"),
                Arguments.of("acfaewnkjnewkf", "12346")
        );
    }

    private static Stream<Arguments> provideHigherReaderCheckUserIdAndValidationTest() {
        return Stream.of(
                Arguments.of("abafewnn", "1234"),
                Arguments.of("aafw3f23", "12345"),
                Arguments.of("acafnwkjnkewfa", "12346")
        );
    }

    private static Stream<Arguments> provideSameUserCheckUserIdAndValidationTest() {
        return Stream.of(
                Arguments.of("absdf", "1234","abc9331","12345"),
                Arguments.of("aaafnanekw", "12345","aabaefw","123456"),
                Arguments.of("acafen", "12346","aacvddfa","123466")
        );
    }
    private static Stream<Arguments> provideUnSameUserCheckUserIdAndValidationTest() {
        return Stream.of(
                Arguments.of("abcde", "abcde00","abcd","abcd00"),
                Arguments.of("aa00", "aa001","aa01","aa011"),
                Arguments.of("ac01id", "ac02pw","ac03id","ac04pw")
        );
    }
    private static Stream<Arguments> provideUserIdAndPassword() {
        return Stream.of(
                Arguments.of("aa", "00"),
                Arguments.of("bb", "11"),
                Arguments.of("cc", "22")
        );
    }


}
