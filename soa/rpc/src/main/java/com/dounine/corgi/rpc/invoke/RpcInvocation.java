package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.invoke.config.NodeInfo;
import com.dounine.corgi.rpc.listen.RpcListener;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.serialize.rmi.IClient;
import com.dounine.corgi.rpc.serialize.rmi.RpcClient;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private Method method;
    private Object[] args;
    private String version;
    private IRegister register;

    public RpcInvocation(Object[] args,Method method,String version){
        this.args = args;
        this.method = method;
        this.version = version;
    }

    public RpcInvocation(String version,IRegister register){
        this.version = version;
        this.register = register;
    }

    @Override
    public IRegister getRegister() {
        return register;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public InetSocketAddress getAddress(Class<T> clazz) {
        NodeInfo nodeInfo = register.getNodeInfo(clazz);
        return new InetSocketAddress(nodeInfo.getHost(),nodeInfo.getPort());
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
        RpcListener.waitRpcListener();//wait rpc listened
        return client.fetch();
    }
}
