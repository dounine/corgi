package com.dounine.corgi.remoting;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/7.
 */
public interface Token {

    Method getMethod();

    Object getInvokeObj();

    String getVersion();

    int getRetries();

    int getTimeout();

    Class<?> getClazz();

    Class<?>[] getParamterTypes();

}
