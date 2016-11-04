package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.serialize.rmi.IClient;
import com.dounine.corgi.rpc.serialize.rmi.RpcClient;
import com.dounine.corgi.rpc.spring.annotation.Autowired;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private Method method;
    private Object[] args;
    private Autowired reference;
    private Balance balance;

    public RpcInvocation(Object[] args,Method method,Autowired reference){
        this.args = args;
        this.method = method;
        this.reference = reference;
    }

    public RpcInvocation(Autowired reference, Balance balance){
        this.reference = reference;
        this.balance = balance;
    }

    @Override
    public Balance getBalance() {
        return balance;
    }

    @Override
    public Autowired getReference() {
        return this.reference;
    }

    @Override
    public InetSocketAddress getAddress(Class<T> clazz) {
        String balanceUrls[] = balance.getBalance("/"+clazz.getName().replace(".","/")).split(":");
        return new InetSocketAddress(balanceUrls[0],Integer.parseInt(balanceUrls[1]));
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
    public IResult fetch(Object[] args,Method method) {
        this.args = args;
        this.method = method;

        IClient client = new RpcClient(this);
        RpcContainer.waitRpcListener();//wait rpc listened
        return client.fetch();
    }
}
