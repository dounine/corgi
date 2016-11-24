package com.dounine.corgi.jta.filter;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by huanghuanlai on 2016/11/18.
 */
public interface JTAApiFilter {

    void methodInvoke();

    void methodBefore(ProceedingJoinPoint joinPoint);

    void methodAfter(ProceedingJoinPoint joinPoint);

    void methodException(ProceedingJoinPoint joinPoint);
}
