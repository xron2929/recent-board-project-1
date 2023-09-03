package com.example.demo.exception;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {
    @GetMapping("/error/401")
    @ApiOperation("잘못된 토큰 정보일 경우 401에러 페이지 반환")
    public String expriationTokenException() {
        return "error/401page";
    }
    @GetMapping("/404page")
    @ApiOperation("삭제되거나 존재하지 않은 boardId 접속시 나타나는 뷰 반환")
    public String error404() {
        return "error/404page";
    }
}
