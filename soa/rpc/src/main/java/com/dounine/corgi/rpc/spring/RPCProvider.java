package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.proxy.RPC;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
public class RPCProvider implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        RPC.export(9999);
    }
}
