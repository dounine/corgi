package com.dounine.corgi.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huanghuanlai on 2016/12/1.
 */
public final class RpcContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcContext.class);
    private static final ThreadLocal<Map<String,Serializable>> ATTACHMENTS = new ThreadLocal<>();

    private RpcContext(){}

    public static final void put(Map<String,Serializable> attachments){
        if(null!=attachments&&attachments.size()>0){
            if(null!=ATTACHMENTS.get()){
                LOGGER.warn("attchments not empty , new attchments overwrited.");
            }
            ATTACHMENTS.set(attachments);
        }
    }

    private static final boolean checkExist(String key){
        if(null!=ATTACHMENTS.get()){
            return ATTACHMENTS.get().containsKey(key);
        }
        return false;
    }

    public static final void put(String key,Serializable val){
        if(null==ATTACHMENTS.get()){
            ATTACHMENTS.set(new HashMap<>(1));
        }
        if(checkExist(key)){
            LOGGER.warn("attchments contains key:"+key+" , new key overwrited exist key.");
        }
        ATTACHMENTS.get().put(key,val);
    }

    public static final Map<String,Serializable> all(){
        return ATTACHMENTS.get();
    }

    public static final int size(){
        Map map  = ATTACHMENTS.get();
        return map==null?0:map.size();
    }

    public static final Serializable get(String key){
        if(null!=ATTACHMENTS.get()){
            return ATTACHMENTS.get().get(key);
        }
        return null;
    }

}
