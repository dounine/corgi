package com.dounine.corgi.rpc;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.filter.ProviderTxFilter;
import com.dounine.corgi.register.Register;
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

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huanghuanlai on 2016/10/18.
 */
public class RpcApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcApp.class);
    public static final Enhancer ENHANCER = new Enhancer();
    private IProtocol protocol;
    private String appName;
    private ProviderFilter providerFilter;
    private ConsumerFilter consumerFilter;
    private ProviderTxFilter providerTxFilter;
    private Register register;
    private static final RpcApp APP = new RpcApp();
    private static final CountDownLatch CONSUMER_FILTER_COUNTDOWNLOAD = new CountDownLatch(1);

    private RpcApp() {
    }

    public final static RpcApp init(IProtocol protocol, Register register, ProviderFilter providerFilter, ConsumerFilter consumerFilter,ProviderTxFilter providerTxFilter, String appName) {
        APP.setRegister(register);
        APP.setProtocol(protocol);
        APP.setProviderFilter(providerFilter);
        APP.setConsumerFilter(consumerFilter);
        APP.setProviderTxFilter(providerTxFilter);
        APP.setAppName(appName);
        CONSUMER_FILTER_COUNTDOWNLOAD.countDown();
        return APP;
    }

    public final static RpcApp instance() {
        return APP;
    }

    public void export() {
        LOGGER.info(getAppName() + " exporting...");
        new Thread(new RpcContainer(protocol, register, providerFilter, consumerFilter,providerTxFilter)).start();
    }

    public <T> T getProxy(Class<T> interfaceClass, Reference reference, Balance balance) {
        Invocation<T> invocation = new RpcInvocation<T>(reference, balance, consumerFilter);
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

    public ProviderFilter getProviderFilter() {
        return providerFilter;
    }

    public void setProviderFilter(ProviderFilter providerFilter) {
        this.providerFilter = providerFilter;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public ConsumerFilter getConsumerFilter() {
        return consumerFilter;
    }

    public void setConsumerFilter(ConsumerFilter consumerFilter) {
        this.consumerFilter = consumerFilter;
    }

    public ProviderTxFilter getProviderTxFilter() {
        return providerTxFilter;
    }

    public void setProviderTxFilter(ProviderTxFilter providerTxFilter) {
        this.providerTxFilter = providerTxFilter;
    }
}
