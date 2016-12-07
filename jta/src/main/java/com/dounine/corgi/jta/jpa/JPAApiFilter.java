package com.dounine.corgi.jta.jpa;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/18.
 */
public interface JPAApiFilter {

    default boolean checkTxTransaction(ProceedingJoinPoint pjp){
        Method meths[] = pjp.getSignature().getDeclaringType().getMethods();
        return meths[0].isAnnotationPresent(Transactional.class);
    }

    void methodBefore(ProceedingJoinPoint pjd);

    void methodAfter(ProceedingJoinPoint pjd, Object result);

    void methodException(ProceedingJoinPoint joinPoint, Throwable ex);
}
