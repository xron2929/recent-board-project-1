package com.example.demo.exception;

import com.example.demo.board.FirstBoardException;
import com.example.demo.join.IsNotExistenceEmailContentException;
import com.example.demo.join.SpecialSymbolException;
import com.example.demo.join.IsNotEqualEmailException;
import com.example.demo.join.isExistenceUserDataException;
import com.example.demo.statistics.ConvertNameException;
import com.example.demo.statistics.TimerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvise {
    @ExceptionHandler({IsNotEqualEmailException.class, SpecialSymbolException.class,
            IsNotExistenceEmailContentException.class, FirstBoardException.class,
            TimerException.class, ConvertNameException.class, isExistenceUserDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> clientDataException(Exception e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> validationException(MethodArgumentNotValidException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        System.out.println("e.getBindingResult().getAllErrors().get(0).getDefaultMessage() = " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
