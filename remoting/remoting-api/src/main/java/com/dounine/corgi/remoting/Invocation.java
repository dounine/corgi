package com.dounine.corgi.remoting;


import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.spring.rpc.Autowired;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Invocation<T> {

    Balance getBalance();

    Autowired getReference();

    InetSocketAddress getAddress(Class<T> clazz);

    Method getMethod();

    Object[] getArgs();

    IResult fetch(Object[] args, Method method);
}
