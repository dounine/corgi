package com.dounine.corgi.filter;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface ProviderFilter {

    void invokeBefore(Method method,Object object, Object[] args);

    void invokeAfter(Object result);

    void invokeError(Throwable throwable);

}
