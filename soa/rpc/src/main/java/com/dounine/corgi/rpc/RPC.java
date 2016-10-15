package com.dounine.corgi.rpc;

import com.dounine.corgi.rpc.interceptor.RpcInterceptor;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
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

    public static void export(final int port) {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("CORGI rpc Invalid port "+port);
        }
        LOGGER.info("CORGI rpc provider port [ "+port+" ] starting.");
        new Thread(new RpcListener(port)).start();
    }

    public static <T> T getProxy(Class<T> interfaceClass){
        ENHANCER.setCallback(new RpcInterceptor(new RpcInvocation<T>()));
        ENHANCER.setInterfaces(new Class<?>[]{interfaceClass});
        return (T) ENHANCER.create();
    }
}
