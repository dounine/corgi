package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.jta.filter.JTAApiFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public class JTAApiFilterImpl implements JTAApiFilter {

    @Autowired
    private JpaTransactionManager jtm;

    @Override
    public void methodInvoke() {

    }

    @Override
    @Before("methodInvoke()")
    public void methodBefore(ProceedingJoinPoint joinPoint) {
        ProviderTXContext.create(jtm);
    }

    @Override
    @After("methodInvoke()")
    public void methodAfter(ProceedingJoinPoint joinPoint) {
        jtm.commit(ProviderTXContext.get());
    }

    @Override
    @AfterThrowing("methodInvoke()")
    public void methodException(ProceedingJoinPoint joinPoint) {
        if(!ProviderTXContext.get().isCompleted()){
            jtm.rollback(ProviderTXContext.get());
        }
    }
}
