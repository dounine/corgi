package com.dounine.corgi.rpc.invoke;


import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.spring.annotation.Reference;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Invocation<T> {

    Balance getBalance();

    Reference getReference();

    InetSocketAddress getAddress(Class<T> clazz);

    Method getMethod();

    Object[] getArgs();

    IResult fetch(Object[] args,Method method);
}
