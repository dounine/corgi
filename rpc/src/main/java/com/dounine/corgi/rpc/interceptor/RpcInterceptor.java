package com.dounine.corgi.rpc.interceptor;

import com.dounine.corgi.rpc.invoke.Invocation;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RpcInterceptor implements MethodInterceptor{

    private final Invocation<?> invoker;

    public RpcInterceptor(final Invocation<?> invoker){
        this.invoker = invoker;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

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
        return invoker.fetch(args,method).result();
    }

}
