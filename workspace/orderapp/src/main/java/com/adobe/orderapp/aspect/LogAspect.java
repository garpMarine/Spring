package com.adobe.orderapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.adobe.orderapp.service.*.*(..))")
    public void logBefore(JoinPoint jp) {
        logger.info("Called : " + jp.getSignature());
        Object[] args = jp.getArgs();
        for(Object arg: args) {
            logger.info("Argument : " + arg);
        }
    }

    @After("execution(* com.adobe.orderapp.service.*.*(..))")
    public void logAfter() {
        logger.info("**************");
    }

    @Around("execution(* com.adobe.orderapp.service.*.*(..))")
    public Object doProfile(ProceedingJoinPoint pjp) throws  Throwable{
        long startTime = new Date().getTime();
            Object ret = pjp.proceed(); // invoke actual called method
        long endTime = new Date().getTime();
        logger.info("Time : " + (endTime  - startTime) + " ms") ;
        return  ret;
    }

    @AfterThrowing(value = "execution(* com.adobe.orderapp.service.*.*(..))", throwing = "ex")
    public  void logException(JoinPoint jp, Exception ex) throws  Exception {
        logger.info("Exception : " + ex.getMessage());
    }
}
