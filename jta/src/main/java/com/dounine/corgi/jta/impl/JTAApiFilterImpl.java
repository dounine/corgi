package com.dounine.corgi.jta.impl;

import com.dounine.corgi.jta.JTAApiFilter;
import com.dounine.corgi.jta.ProviderJTATXContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.jta.JtaTransactionManager;

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
            ProviderJTATXContext.create(jtm);
        }
    }

    @Override
    public void methodAfter(ProceedingJoinPoint pjp, Object result) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api execute finish.");
            for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                jtatx.getClient().txCall(jtatx.getFetchToken(),"commit");
            }
            jtm.commit(ProviderJTATXContext.get());
        }
    }

    @Override
    public void methodException(ProceedingJoinPoint jp, Throwable ex) {
        if(checkTxTransaction(jp)){
            if(!ProviderJTATXContext.get().isCompleted()){
                LOGGER.info("CORGI JTA local api rollback.");
                for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                    jtatx.getClient().txCall(jtatx.getFetchToken(),"rollback");
                }
                jtm.rollback(ProviderJTATXContext.get());
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
