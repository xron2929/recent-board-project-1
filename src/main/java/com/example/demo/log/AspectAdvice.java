package com.example.demo.log;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(0)
@Component
@Aspect
public class AspectAdvice {
    @Pointcut("execution(* com.example.demo.security.jwt..*.*(..))")
    public void jwtPackageExecution() {}

    public Object doTransaction(ProceedingJoinPoint joinPoint) throws  Throwable {
        try {
            // @Before
            log.info("[transaction start] {}", joinPoint.getSignature());
            log.info("{} first",joinPoint.getSignature());
            System.out.println("joinPoint.getSignature() = " + joinPoint.getSignature());
            Object result = joinPoint.proceed();

            // @AfterReturning
            log.info("[transaction end] {}", joinPoint.getSignature());
            return result;
        } catch (Exception ex) {
            // @AfterThrowing
            log.info("[transaction rollback] {}", joinPoint.getSignature());
            throw ex;
        } finally {
            // @After
            log.info("[resource release] {}", joinPoint.getSignature());
            log.info("{} final",joinPoint.getSignature());
        }
    }
}

