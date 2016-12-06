package com.dounine.corgi.jta.filter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by huanghuanlai on 2016/11/18.
 */
public interface JTAApiFilter {

    void methodBefore(ProceedingJoinPoint pjd);

    void methodAfter(ProceedingJoinPoint pjd, Object result);

    void methodException(JoinPoint joinPoint, Throwable ex);
}
