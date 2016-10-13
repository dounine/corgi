package com.dounine.corgi.rpc.invoke;

import com.dounine.corgi.rpc.serialize.Result;
import com.dounine.corgi.rpc.serialize.RpcClient;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcInvocation<T> implements Invocation<T> {

    private InetSocketAddress address;
    private Method method;
    private Object[] args;
    private Class<T> interfaceClass;
    private String version;

    public RpcInvocation(){
        this.address = new InetSocketAddress("localhost",9999);//set default
        this.version = "1.0.0";
    }

    public RpcInvocation(String host,int port,Class<T> interfaceClass,String version){
        this.address = new InetSocketAddress(host,port);
        this.interfaceClass = interfaceClass;
        this.version = version;
    }

    public RpcInvocation(InetSocketAddress address,Method method,Object[] args,Class<T> interfaceClass,String version){
        this.address = address;
        this.method = method;
        this.args = args;
        this.interfaceClass = interfaceClass;
        this.version = version;
    }

    @Override
    public Class<T> getInterfaceClass() {
        return this.interfaceClass;
    }

    @Override
    public String getVersion() {
        return this.version;
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
    public Result invoke(Invocation invoker) {
        RpcClient rpcClient = new RpcClient(invoker);
        return rpcClient.fetch();
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
