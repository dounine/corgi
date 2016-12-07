package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.context.ApiContext;
import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.filter.ProviderTxFilter;
import com.dounine.corgi.filter.impl.DefaultConsumerFilter;
import com.dounine.corgi.remoting.*;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.spring.rpc.Reference;
import com.dounine.corgi.spring.rpc.Service;
import com.dounine.corgi.utils.ClassUtils;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private Method method;
    private Object[] args;
    private Reference reference;
    private Balance balance;
    private ConsumerFilter consumerFilter;
    private ProviderTxFilter providerTxFilter;

    public RpcInvocation(Object[] args, Method method, Reference reference, ConsumerFilter consumerFilter, ProviderTxFilter providerTxFilter) {
        this.args = args;
        this.method = method;
        this.reference = reference;
        this.consumerFilter = consumerFilter;
        this.providerTxFilter = providerTxFilter;
    }

    public RpcInvocation(Reference reference, Balance balance, ConsumerFilter consumerFilter, ProviderTxFilter providerTxFilter) {
        this.reference = reference;
        this.balance = balance;
        this.consumerFilter = consumerFilter;
        this.providerTxFilter = providerTxFilter;
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
        FetchToken token = consumerFilter.getToken(client, ApiContext.getTxID());
        Result result = null;
        try {
            result = consumerFilter.getResult(client, token);

            if(token.checkCommit()){
                consumerFilter.execTransaction(client, token, "commit");
            }
        } catch (Throwable e) {
            if(token.checkCommit()){
                consumerFilter.execTransaction(client, token, "rollback");
            }
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

    public ProviderTxFilter getProviderTxFilter() {
        return providerTxFilter;
    }

    public void setProviderTxFilter(ProviderTxFilter providerTxFilter) {
        this.providerTxFilter = providerTxFilter;
    }
}
