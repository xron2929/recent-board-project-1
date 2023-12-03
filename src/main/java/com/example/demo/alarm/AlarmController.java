package com.example.demo.alarm;

import com.example.demo.board.BoardService;
import com.example.demo.util.UserIdAndValidationDtoAndAccessToken;
import com.example.demo.util.UserManager;
import com.example.demo.util.ValidationStatus;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.entityjoin.NoneUserUuidANdTitleAndPasswordDto;
import com.example.demo.boradAndUser.TitleAndUserIdDto;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.user.UserService;
import com.example.demo.util.PageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AlarmController {

    @Autowired
    AlarmService alarmService;
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;
    @Autowired
    NoneUserAlarmService noneUserAlarmService;
    @Autowired
    UserManager userManager;
    @GetMapping("/alarm")
    @ApiOperation("alarm 뷰 반환")
    public String getAlarmView(@RequestParam(defaultValue = "0") Long page) {
        return "alarm/alarm";
    }
    // 알람서비스에서 데이터는 10개씩 보여주는 걸로 하고 이제 이걸 번호 달고 API 등록할 예정임
    @GetMapping("/alarm/data/{page}")
    @ApiOperation("해당 id의 alarm 목록 찾기")
    @ResponseBody
    public List<Alarm> getData(HttpServletRequest request, @PathVariable int page) throws JsonProcessingException {
        UserIdAndValidationDtoAndAccessToken userIdAndValidationDtoAndAccessToken = userManager.getUserIdAndValidationDtoAndAccessToken(request);
        String dbPassword = null;
        if(userIdAndValidationDtoAndAccessToken.getValidationStatus()!= ValidationStatus.NONE_USER_ACCOUNT) {
            dbPassword = userService.findPassword(userIdAndValidationDtoAndAccessToken.getUserId());
        }
        UserIdAndValidationDtoAndAccessToken userAccountStatus = userManager.getUserAccountStatus(request, userIdAndValidationDtoAndAccessToken, dbPassword);
        Pageable pageable = PageRequest.of(page-1, 10);
        if(userAccountStatus.getValidationStatus() != null && userAccountStatus.getValidationStatus() != ValidationStatus.ERROR_ACCOUNT) {
            return alarmService.findAlarmData(pageable, userIdAndValidationDtoAndAccessToken.getUserId());
        }
        return null;
    }




    @PutMapping("/alarm/visited/{alarmId}")
    @ApiOperation("alarm 조회시 흐리게 보여주게 할 것이기 때문에 해당 페이지 방문했다고 Db에 업데이트")
    @ResponseBody
    public String checkVisited(@PathVariable Long alarmId) {
        alarmService.changeAlarm(alarmId);
        return "ok";
    }
    @Autowired
    JwtManager jwtManager;
    @Autowired
    CookieManager cookieManager;

    @PostMapping("/alarm/user")
    @ApiOperation("Alarm 에서 연결하는 권한 중 유저인 데이터에 저장 ")
    @ResponseBody
    public String setAlarm(@RequestBody AlarmSaveDto alarmSaveDto, HttpServletRequest request) throws JsonProcessingException {
        System.out.println("호출");
       TitleAndUserIdDto titleByBoardId = boardService.findUserDto(alarmSaveDto.getBoardId());
        // id,password 조회 후 실제 db랑 같으면 저장
        // 사실 REdis 쓰면 이렇게 검증할 이유가 없는데,..

        Alarm alarm = Alarm.builder()
                .title(titleByBoardId.getTitle())
                .boardWriterId(titleByBoardId.getUserId())
                .boardId(alarmSaveDto.getBoardId())
                .isVisited(alarmSaveDto.getIsVisited())
                .summaryCommentContent(alarmSaveDto.getSummaryCommentContent())
                .commentWriter(alarmSaveDto.getCommentWriter())
                .build();
        alarmService.saveAlarm(alarm);
        return "ok";
    }
    @PostMapping("/alarm/none-user")
    @ApiOperation("Alarm 에서 연결하는 권한 중 유저가 아닌 데이터에 저장 ")
    @ResponseBody
    public String setNoneUserAlarm(@RequestBody NoneUserAlarmSaveDto noneUserAlarmSaveDto, HttpServletRequest request) throws JsonProcessingException {
        // uuid는 보안상 노출되어도 큰 위협은 안된다고 판단함
        // 그래도 보이는 건 일단 막아야될듯
        NoneUserUuidANdTitleAndPasswordDto noneUserDto =
                boardService.findNoneUserDto(noneUserAlarmSaveDto.getBoardId());

        String uuidCookie = cookieManager.getUUidCookie(request);
        System.out.println("AlarmController - setNoneUserAlarm() uuidCookie = "+uuidCookie);
        Alarm alarm = NoneUserAlarm.builder()
                // boardId -> userId -> uuid 정도로 노출
                .userName(noneUserDto.getUserId())
                .title(noneUserDto.getTitle())
                .boardWriterId(noneUserDto.getUuid())
                .boardId(noneUserAlarmSaveDto.getBoardId())
                .isVisited(noneUserAlarmSaveDto.getIsVisited())
                .summaryCommentContent(noneUserAlarmSaveDto.getSummaryCommentContent())
                .boardWriterId(noneUserAlarmSaveDto.getBoardWriterId())
                .commentWriter(uuidCookie)
                .build();
        alarmService.saveAlarm(alarm);
        return "ok";
    }
    @GetMapping("/current/page")
    @ApiOperation("모든 alarm 데이터의 갯수를 가져옴")
    @ResponseBody
    public PageDto getPage(@RequestParam String userId, @RequestParam Long currentPageNumber) {
        Long dataSize = alarmService.getAlarmSize(userId);
        PageCalculator pageCalculator = new PageCalculator(userId,currentPageNumber,10l,dataSize);
        return pageCalculator.getPage();
    }
}
