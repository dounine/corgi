package com.dounine.corgi.register;

import java.util.Set;

/**
 * Created by huanghuanlai on 2016/10/20.
 */
public interface Register {

    void register(Class<?> clazz,String nodeInfo);

    Set<Class<?>> getRegisterClass();

}
