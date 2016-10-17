package com.dounine.corgi.rpc;

import com.dounine.corgi.rpc.interceptor.RpcInterceptor;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
import com.dounine.corgi.rpc.invoke.config.IProtocol;
import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.invoke.config.RegisterConfig;
import com.dounine.corgi.rpc.listen.RpcListener;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RPC {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPC.class);

    public static final Enhancer ENHANCER = new Enhancer();

    public static void export(IProtocol protocolConfig) {
        new Thread(new RpcListener(protocolConfig)).start();
    }

    public static <T> T getProxy(Class<T> interfaceClass, IRegister register){
        ENHANCER.setCallback(new RpcInterceptor(new RpcInvocation<T>(register),register));
        ENHANCER.setInterfaces(new Class<?>[]{interfaceClass});
        return (T) ENHANCER.create();
    }
}
