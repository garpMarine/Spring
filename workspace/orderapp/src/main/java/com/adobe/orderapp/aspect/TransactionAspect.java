package com.adobe.orderapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(Tx)")
    public Object doTransaction(ProceedingJoinPoint pjp) throws  Throwable{
        Object ret = null;
        try {
            logger.info("Begin Transaction...");
            ret = pjp.proceed();
            logger.info("Commit tx");
        }catch (Exception ex) {
            logger.info("Rollback tx");
        }
        return  ret;
    }
}
