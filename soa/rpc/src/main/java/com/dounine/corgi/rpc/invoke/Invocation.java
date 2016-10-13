package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.rpc.serialize.Result;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Invocation<T> {

    Class<T> getInterfaceClass();

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



    Result invoke(Invocation<T> invoker);
}
