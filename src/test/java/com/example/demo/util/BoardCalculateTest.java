package com.example.demo.util;

import com.example.demo.security.jwt.UserRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;
@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
public class BoardCalculateTest {



    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    @DisplayName("board 계산 가능")
    void getStartBoardQuantityTest(int pageQuantity,int boardQuantity,int expectedResult) {
            //given
                BoardCalculator boardCalculator = new BoardCalculator();
            //when
                BoardQueryDto calculate = boardCalculator.calculate(pageQuantity, boardQuantity);
            //then
                Assertions.assertThat(calculate.getPrevBoardQuantity()).isEqualTo(expectedResult);
        }
    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of(1, 20, 0),
                Arguments.of(2, 10, 10),
                Arguments.of(2, 15, 15)
        );
    }

    public UserIdAndValidationDtoAndAccessToken validateSiteOrAdminPasswordTest(String userId, String password, String dbPassword) {
        if(password.equals(dbPassword)) {
            return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.USER_ACCOUNT,userId);
        }
        return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.ERROR_ACCOUNT,userId);
    }
}
