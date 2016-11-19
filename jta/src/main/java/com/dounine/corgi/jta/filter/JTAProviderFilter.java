package com.dounine.corgi.jta.filter;

import com.dounine.corgi.filter.ProviderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.*;
import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
@Component
public class JTAProviderFilter implements ProviderFilter {

    @Autowired
    private TransactionManager tm;

    @Override
    public void invokeBefore(Method method, Object object, Object[] args) {
        try {
            tm.begin();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invokeAfter(Object result) {
        try {
            tm.commit();
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

    @Override
    public void invokeError(Throwable throwable) {
        try {
            tm.rollback();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }
}
