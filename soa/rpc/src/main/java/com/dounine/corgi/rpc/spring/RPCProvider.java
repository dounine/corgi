package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.RPC;
import com.dounine.corgi.rpc.listen.RpcListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
public class RpcProvider extends RpcConPro implements ApplicationListener,IRpcConPro {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(!RpcListener.isListener()){
            RPC.export(getPort());
        }
    }

}
