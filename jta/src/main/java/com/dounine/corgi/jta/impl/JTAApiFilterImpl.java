package com.dounine.corgi.jta.impl;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.dounine.corgi.jta.JTAApiFilter;
import com.dounine.corgi.jta.ProviderJTATXContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public abstract class JTAApiFilterImpl implements JTAApiFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JTAApiFilterImpl.class);
    @Autowired
    private UserTransactionManager utm;
    @Autowired
    private JTAConsumerFilterImpl jtaConsumerFilter;

    @Override
    public void methodBefore(ProceedingJoinPoint pjp) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api create.");
            try {
                utm.begin();
            } catch (SystemException e) {
                e.printStackTrace();
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void methodAfter(ProceedingJoinPoint pjp, Object result) {
        if(checkTxTransaction(pjp)){
            LOGGER.info("CORGI JTA local api execute finish.");
            for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                jtatx.getClient().txCall(jtatx.getFetchToken(),"commit");
            }
            try {
                utm.commit();
            } catch (RollbackException e) {
                e.printStackTrace();
            } catch (HeuristicMixedException e) {
                e.printStackTrace();
            } catch (HeuristicRollbackException e) {
                e.printStackTrace();
            } catch (SystemException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void methodException(ProceedingJoinPoint jp, Throwable ex) {
        if(checkTxTransaction(jp)){
            if(null!=ProviderJTATXContext.get()){
                LOGGER.info("CORGI JTA local api rollback.");
                for(JTATX jtatx : jtaConsumerFilter.getJtaTxs()){
                    jtatx.getClient().txCall(jtatx.getFetchToken(),"rollback");
                }
                try {
                    utm.rollback();
                } catch (SystemException e) {
                    e.printStackTrace();
                }
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
