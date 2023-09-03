package com.example.demo.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogAdvice {
    // Around면 해야되지만 ProceddingJoinpoint 했음 Before 할꺼라
    @Around("@annotation(Trace)")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("\nstart |==============="+joinPoint.getSignature()+"==============|\n");
        // log.info("===============\"{}\"==============",joinPoint.getTarget());
        Object proceed = joinPoint.proceed();
        System.out.println("\nend |==============="+joinPoint.getSignature()+"==============|\n");
        return proceed;
    }
    void dsfx(){
        System.out.println("sdfadsfs");
    }
}
