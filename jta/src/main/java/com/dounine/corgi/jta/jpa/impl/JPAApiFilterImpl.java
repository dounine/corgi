package com.dounine.corgi.jta.jpa.impl;

import com.dounine.corgi.jta.jpa.JPAApiFilter;
import com.dounine.corgi.jta.jpa.ProviderJPATXContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public abstract class JPAApiFilterImpl implements JPAApiFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAApiFilterImpl.class);
    @Autowired
    private JpaTransactionManager jtm;
    @Autowired
    private JPAConsumerFilterImpl jtaConsumerFilter;

    @Override
    public void methodBefore(ProceedingJoinPoint pjp) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api create.");
            ProviderJPATXContext.create(jtm);
        }
    }

    @Override
    public void methodAfter(ProceedingJoinPoint pjp, Object result) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api execute finish.");
            for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                jtatx.getClient().txCall(jtatx.getFetchToken(),"commit");
            }
            jtm.commit(ProviderJPATXContext.get());
        }
    }

    @Override
    public void methodException(ProceedingJoinPoint jp, Throwable ex) {
        if(checkTxTransaction(jp)){
            if(!ProviderJPATXContext.get().isCompleted()){
                LOGGER.info("CORGI JTA local api rollback.");
                for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                    jtatx.getClient().txCall(jtatx.getFetchToken(),"rollback");
                }
                jtm.rollback(ProviderJPATXContext.get());
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
