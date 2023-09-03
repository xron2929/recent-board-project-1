package com.example.demo.join;

import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// @Component
public class EmailVaildator {
    @Autowired UserService userService;
    private static final String SPECIAL_SYMBOLS[] = {
            "-","^","+","-","=","*","/"
    };
    public void validation(String emailData,JoinDto joinDto) throws isExistenceUserDataException, IsNotExistenceEmailContentException, IsNotEqualEmailException, SpecialSymbolException {
       if(userService.isExistenceUserIdOrEmailOrPhoneNumber(joinDto.getUserId(),joinDto.getEmail(),joinDto.getPhoneNumber())) {
           throw new isExistenceUserDataException("입력한 정보 중 동일한 계정이 존재합니다");
       }
       if(emailData == null) {
           throw new IsNotExistenceEmailContentException("해당 이메일로 데이터를 요청한 기록이 없거나 요청이 만료되었습니다");
       }
       if(!emailData.equals(joinDto.getEmailCode())) {
           throw new IsNotEqualEmailException("이메일 인증 번호가 같지 않습니다");
       }
       if(isIncludingSpecialSymbolInUserId(joinDto.getUserId())) {
           throw new SpecialSymbolException("userId에 특수문자가 포함되어 있습니다");
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


}
