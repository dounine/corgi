package com.dounine.corgi.rpc.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public final class ThreadPools {
    private static ExecutorService executors;
    private ThreadPools(){}

    static {
        executors = Executors.newFixedThreadPool(10);
    }

    public static ExecutorService getExecutor(){
        return executors;
    }

}
