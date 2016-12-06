package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.context.ApiContext;
import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.filter.impl.DefaultConsumerFilter;
import com.dounine.corgi.remoting.*;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.spring.rpc.Reference;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private Method method;
    private Object[] args;
    private Reference reference;
    private Balance balance;
    private ConsumerFilter consumerFilter;

    public RpcInvocation(Object[] args, Method method, Reference reference,ConsumerFilter consumerFilter) {
        this.args = args;
        this.method = method;
        this.reference = reference;
        this.consumerFilter = consumerFilter;
    }

    public RpcInvocation(Reference reference, Balance balance,ConsumerFilter consumerFilter) {
        this.reference = reference;
        this.balance = balance;
        this.consumerFilter = consumerFilter;
    }

    @Override
    public Balance getBalance() {
        return balance;
    }

    @Override
    public Reference getReference() {
        return this.reference;
    }

    @Override
    public InetSocketAddress getAddress(Class<T> clazz) {
        String balanceUrls[] = balance.getBalance("/" + clazz.getName().replace(".", "/")).split(":");
        return new InetSocketAddress(balanceUrls[0], Integer.parseInt(balanceUrls[1]));
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public Result fetch(Object[] args, Method method) {
        this.args = args;
        this.method = method;

        FetchRemoting client = new P2PFetchRemoting(this);
        RpcContainer.waitRpcListener();//wait rpc listened
        FetchToken token = null;
        if (null == consumerFilter) {
            consumerFilter = new DefaultConsumerFilter();
        }
        token = consumerFilter.getToken(client, ApiContext.getTxID());
        Result result = null;
        try {
            result = consumerFilter.getResult(client, token);
            consumerFilter.execTransaction(client, token, "commit");
        } catch (Throwable e) {
            consumerFilter.execTransaction(client, token, "rollback");
            throw e;
        }
        return result;
    }

    public ConsumerFilter getConsumerFilter() {
        return consumerFilter;
    }

    public void setConsumerFilter(ConsumerFilter consumerFilter) {
        this.consumerFilter = consumerFilter;
    }
}
