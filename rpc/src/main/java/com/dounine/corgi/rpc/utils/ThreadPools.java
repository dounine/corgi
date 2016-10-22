package com.dounine.corgi.rpc.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public final class ThreadPools {
    private ThreadPools(){}

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(RpcProperties.instance().getInteger("corgi.provider.threads"));

    public static ExecutorService getExecutor(){
        return EXECUTOR_SERVICE;
    }

}
