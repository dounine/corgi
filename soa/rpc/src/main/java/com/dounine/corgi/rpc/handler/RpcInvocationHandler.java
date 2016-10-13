package com.dounine.corgi.rpc.handler;

import com.dounine.corgi.rpc.invoke.Invocation;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
import com.dounine.corgi.rpc.serialize.Result;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RpcInvocationHandler implements InvocationHandler{

    private Invocation<?> invoker;

    public RpcInvocationHandler(Invocation<?> invoker){
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setMethod(method);
        rpcInvocation.setInterfaceClass(proxy.getClass().getInterfaces()[0]);
        rpcInvocation.setVersion(invoker.getVersion());

        Result result = invoker.invoke(rpcInvocation);
        if(result.hasError()){
            throw result.exception();
        }
        return result.data();
    }
}
