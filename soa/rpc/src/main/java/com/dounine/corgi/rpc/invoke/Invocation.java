package com.dounine.corgi.rpc.invoke;


import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.serialize.result.IResult;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Invocation<T> {

    IRegister getRegister();

    String getVersion();

    /**
     * get invoke fetch address
     * @return address:localhost,port
     */
    InetSocketAddress getAddress(Class<T> clazz);

    /**
     * get invoke execute method
     * @return method
     */
    Method getMethod();

    /**
     * get invoke execute method args
     * @return args
     */
    Object[] getArgs();



    IResult fetch(Object[] args,Method method);
}
