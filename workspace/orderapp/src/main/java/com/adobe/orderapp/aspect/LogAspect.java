package com.adobe.orderapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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


}
