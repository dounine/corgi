package com.dounine.corgi.filter;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface ProviderFilter {

    default void invokeBefore(Method method,Object object, Object[] args){

    }

    default void invokeAfter(Object result){
    }

    default void invokeError(Throwable throwable){

    }

    default void callback(String txType) throws Exception{

    }

}
