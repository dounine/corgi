package com.dounine.corgi.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by huanghuanlai on 2016/11/29.
 */
public final class ApiContext {

    private static final ThreadLocal<String> API_METHOD_TX_ID = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> API_METHOD_RPC_ADDRESS = new ThreadLocal<>();

    private ApiContext(){}

    public static final void setTxID(String txId){
        API_METHOD_TX_ID.set(txId);
    }


    /**
     * 获取API 调用方法事务ID
     * @return 方法事务ID
     */
    public static final String getTxID(){
        return API_METHOD_TX_ID.get();
    }

    public static final Set<String> getMethodRpcAddress(){
        return API_METHOD_RPC_ADDRESS.get();
    }

    /**
     * 获取API 调用方法地扯
     * @return ip地扯及端口
     */
    public static final void addMethodRpcAddress(String address){
        if(null==API_METHOD_RPC_ADDRESS.get()){
            API_METHOD_RPC_ADDRESS.set(new HashSet<>());
        }
        API_METHOD_RPC_ADDRESS.get().add(address);
    }
}
