package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 2016/10/23.
 */
public interface PushData {

    Class<?> getClazz();

    Class<?>[] getParamterTypes();

    Object[] getObjects();

    String getMethodName();

}
