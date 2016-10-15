package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.rpc.listen.RpcListener;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.serialize.rmi.IClient;
import com.dounine.corgi.rpc.serialize.rmi.RpcClient;
import com.dounine.corgi.rpc.utils.RpcContext;
import com.dounine.corgi.rpc.utils.VersionContext;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private InetSocketAddress address;
    private Method method;
    private Object[] args;

    public RpcInvocation(Object[] args,Method method){
        initAddressPort();
        this.args = args;
        this.method = method;
    }

    public void initAddressPort(){
        this.address = new InetSocketAddress(RpcContext.currentHost(),RpcContext.currentPort());//set default
    }

    public RpcInvocation(){
        initAddressPort();
    }

    @Override
    public String getVersion() {
        return VersionContext.currentVersion();
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
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
    public IResult invoke(Invocation invoker) {
        IClient client = new RpcClient(invoker);
        RpcListener.waitRpcListener();//wait rpc listened
        return client.fetch();
    }
}
