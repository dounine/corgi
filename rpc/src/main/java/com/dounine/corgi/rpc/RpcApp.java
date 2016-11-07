package com.dounine.corgi.rpc;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.remoting.Invocation;
import com.dounine.corgi.rpc.interceptor.RpcInterceptor;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.rpc.protocol.IProtocol;
import com.dounine.corgi.spring.rpc.Reference;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huanghuanlai on 2016/10/18.
 */
public class RpcApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcApp.class);
    public static final Enhancer ENHANCER = new Enhancer();
    private IProtocol protocol;
    private String appName;
    private static final RpcApp APP = new RpcApp();

    private RpcApp() {
    }

    public final static RpcApp init(IProtocol protocol,String appName) {
        APP.setProtocol(protocol);
        APP.setAppName(appName);
        return APP;
    }

    public final static RpcApp instance() {
        return APP;
    }

    public void export() {
        LOGGER.info(getAppName() + " exporting...");
        new Thread(new RpcContainer(getProtocol())).start();
    }

    public <T> T getProxy(Class<T> interfaceClass, Reference reference, Balance balance) {
        Invocation<T> invocation = new RpcInvocation<T>(reference, balance);
        Callback callback = new RpcInterceptor(invocation);
        ENHANCER.setCallback(callback);
        ENHANCER.setInterfaces(new Class<?>[]{interfaceClass});
        return (T) ENHANCER.create();
    }

    public IProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(IProtocol protocol) {
        this.protocol = protocol;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
