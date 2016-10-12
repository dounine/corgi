package com.dounine.corgi.rpc.proxy;

import com.dounine.corgi.rpc.handler.RpcInvocationHandler;
import com.dounine.corgi.rpc.invoke.RpcInvocation;
import com.dounine.corgi.rpc.serialize.RpcServer;
import com.dounine.corgi.rpc.utils.ThreadPools;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RPC {

    public static void export(final int port) {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("CORGI rpc Invalid port"+port);
        }

        System.out.println("CORGI rpc provider port:"+port+" started.\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    for(;;){
                        socket = serverSocket.accept();
                        System.out.println("CORGI "+socket.getInetAddress()+" rpc consumer connected.");
                        ThreadPools.getExecutor().execute(new RpcServer(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static <T> T getProxy(Class<T> interfaceClass,String host,int port,String version){
        if(null==interfaceClass){
            throw new IllegalArgumentException("interface not null");
        }
        if(null==host){
            throw new IllegalArgumentException("host not null");
        }
        if(port<=0||port>65535){
            throw new IllegalArgumentException("Invalid port "+port);
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},new RpcInvocationHandler(new RpcInvocation<T>(host,port,interfaceClass,version)));
    }
}
