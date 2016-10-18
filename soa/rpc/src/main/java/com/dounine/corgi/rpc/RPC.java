package com.dounine.corgi.rpc;

import com.dounine.corgi.rpc.interceptor.RpcInterceptor;
import com.dounine.corgi.rpc.invoke.Invocation;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
import com.dounine.corgi.rpc.invoke.config.IProtocol;
import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.listen.RpcListener;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RPC {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPC.class);

    public static final Enhancer ENHANCER = new Enhancer();

    public static void export(IProtocol protocol) {
        new Thread(new RpcListener(protocol)).start();
    }

    public static <T> T getProxy(Class<T> interfaceClass,String version, IRegister register){
        Invocation<T> invocation = new RpcInvocation<T>(version,register);
        Callback callback = new RpcInterceptor(invocation);
        ENHANCER.setCallback(callback);
        ENHANCER.setInterfaces(new Class<?>[]{interfaceClass});
        return (T) ENHANCER.create();
    }
}
