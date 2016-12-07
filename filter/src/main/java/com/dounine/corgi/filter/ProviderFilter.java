package com.dounine.corgi.filter;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface ProviderFilter {

    default boolean checkTxTransaction(Method method) {
        return method.isAnnotationPresent(org.springframework.transaction.annotation.Transactional.class);
    }

    default void invokeBefore(Method method,Object object, Object[] args){

    }

    default void invokeAfter(Method method,Object result){
    }

    default void invokeError(Throwable throwable){

    }

    default void callback(String txType) throws Exception{

    }

}
