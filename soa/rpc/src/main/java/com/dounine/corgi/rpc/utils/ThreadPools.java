package com.dounine.corgi.rpc.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public final class ThreadPools {
    private ThreadPools(){}

    private static ExecutorService executors;

    static {
        executors = Executors.newFixedThreadPool(20);
    }

    public static ExecutorService getExecutor(){
        return executors;
    }

}
