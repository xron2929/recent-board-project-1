package com.example.demo.user.join;

import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

// @Component
public class EmailValidator {
    @Autowired UserService userService;
    private static final String SPECIAL_SYMBOLS[] = {
            "-","^","+","-","=","*","/"
    };
    public void validation(String emailData,JoinDto joinDto,String duplicatePhoneNumberAndNicknameResult) throws isExistenceUserDataException, IsNotExistenceEmailContentException, IsNotEqualEmailException, SpecialSymbolException {
        if(emailData == null) {
            throw new IsNotExistenceEmailContentException("해당 이메일로 데이터를 요청한 기록이 없거나 요청이 만료되었습니다");
        }
        if(!emailData.equals(joinDto.getEmailCode())) {
            throw new IsNotEqualEmailException("이메일 인증 번호가 같지 않습니다");
        }
        if(isIncludingSpecialSymbolInUserId(joinDto.getUserId())) {
            throw new SpecialSymbolException("userId에 특수문자가 포함되어 있습니다");
        }
        if(duplicatePhoneNumberAndNicknameResult!="ok") {
            throw new isExistenceUserDataException(duplicatePhoneNumberAndNicknameResult);
        }
       return;
   }
    private boolean isIncludingSpecialSymbolInUserId(String userId) {
        for (String SPECIAL_SYMBOL:SPECIAL_SYMBOLS){
            if(userId.contains(SPECIAL_SYMBOL)) {
                return true;
            }
        }
        return false;
    }

    public String isExistencePhoneNumberOrNickname(String duplicatePhoneNumberAndNicknameResult) throws isExistenceUserDataException {

        System.out.println("JoinController - isAllowedJoin() - duplicatePhoneNumberAndNicknameResult = " + duplicatePhoneNumberAndNicknameResult);
        if(duplicatePhoneNumberAndNicknameResult == null || duplicatePhoneNumberAndNicknameResult == "") {
            return "ok";
        }
        throw new isExistenceUserDataException(duplicatePhoneNumberAndNicknameResult);
    }


}
