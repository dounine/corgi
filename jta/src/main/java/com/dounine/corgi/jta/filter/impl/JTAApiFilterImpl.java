package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.jta.filter.JTAApiFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public abstract class JTAApiFilterImpl implements JTAApiFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JTAApiFilterImpl.class);
    @Autowired
    private JtaTransactionManager jtm;
    @Autowired
    private JTAConsumerFilterImpl jtaConsumerFilter;

    @Override
    public void methodBefore(ProceedingJoinPoint pjp) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api create.");
            ProviderTXContext.create(jtm);
        }
    }

    @Override
    public void methodAfter(ProceedingJoinPoint pjp, Object result) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api execute finish.");
            for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                jtatx.getClient().txCall(jtatx.getFetchToken(),"commit");
            }
            jtm.commit(ProviderTXContext.get());
        }
    }

    @Override
    public void methodException(ProceedingJoinPoint jp, Throwable ex) {
        if(checkTxTransaction(jp)){
            if(!ProviderTXContext.get().isCompleted()){
                LOGGER.info("CORGI JTA local api rollback.");
                for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                    jtatx.getClient().txCall(jtatx.getFetchToken(),"rollback");
                }
                jtm.rollback(ProviderTXContext.get());
            }else{
                LOGGER.info("CORGI JTA local api tx is complted.");
            }
        }
    }

    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        Object result = null;
        //执行目标方法
        try {
            //前置通知
            this.methodBefore(pjd);
            result = pjd.proceed();
            //返回通知
            this.methodAfter(pjd, result);
        } catch (Throwable e) {
            this.methodException(pjd, e);
            //异常通知
            throw e;
        }
        //后置通知
        return result;
    }
}
