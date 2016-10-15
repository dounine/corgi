package com.dounine.corgi.rpc.invoke;


import com.dounine.corgi.rpc.serialize.result.IResult;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Invocation<T> {

    String getVersion();

    /**
     * get invoke fetch address
     * @return address:localhost,port
     */
    InetSocketAddress getAddress();

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



    IResult invoke(Invocation<T> invoker);
}
