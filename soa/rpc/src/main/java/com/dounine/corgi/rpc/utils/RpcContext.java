package com.dounine.corgi.rpc.utils;

import com.dounine.corgi.rpc.zk.ZkClient;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public final class RpcContext {

    private static int PORT = 3333;
    private static String HOST = "localhost";
    private static boolean useZk = false;
    private static ZkClient zkClient = null;

    private RpcContext(){}
    public static void init(String host,int port){
        if(null==host){
            throw new IllegalArgumentException("host not null");
        }
        if(port<=0||port>65535){
            throw new IllegalArgumentException("Invalid port "+ port);
        }
        RpcContext.HOST = host;
        RpcContext.PORT = port;
    }

    public static int currentPort(){
        return RpcContext.PORT;
    }

    public static String currentHost(){
        return RpcContext.HOST;
    }

    public static boolean isUseZk() {
        return useZk;
    }

    public static void setUseZk(boolean useZk) {
        RpcContext.useZk = useZk;
    }
}
