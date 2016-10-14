package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.proxy.RPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
public class RPCProvider implements ApplicationListener {

    @Autowired
    protected Environment env;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        RPC.export(getExport());
    }

    public int getExport(){
        return Integer.parseInt(env.getProperty("corgi.rpc.rmi.port","9999"));
    }
}
