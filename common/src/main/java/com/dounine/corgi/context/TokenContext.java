package com.dounine.corgi.context;

/**
 * Created by huanghuanlai on 2016/11/30.
 */
public interface TokenContext {
    ThreadLocal<String> LOCAL_TOKEN = new ThreadLocal<>();

    public static void set(String token){
        LOCAL_TOKEN.set(token);
    }

    public static String get(){
        return LOCAL_TOKEN.get();
    }
}
