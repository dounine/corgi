package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.invoke.config.NodeInfo;
import com.dounine.corgi.rpc.listen.RpcListener;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.serialize.rmi.IClient;
import com.dounine.corgi.rpc.serialize.rmi.RpcClient;
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
    private IRegister register;

    public RpcInvocation(Object[] args,Method method,final IRegister register){
        this.args = args;
        this.method = method;
        this.register = register;
    }

    public RpcInvocation(IRegister register){
        this.register = register;
    }

    @Override
    public String getVersion() {
        return VersionContext.currentVersion();
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
    public IResult invoke(Invocation invoker) {
        IClient client = new RpcClient(invoker);
        RpcListener.waitRpcListener();//wait rpc listened
        return client.fetch();
    }
}
