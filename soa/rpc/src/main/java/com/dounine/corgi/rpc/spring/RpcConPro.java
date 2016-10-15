package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.utils.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class RpcConPro implements IRpcConPro {

    @Autowired
    private Environment env;

    @PostConstruct
    public void init(){
        RpcContext.init(getHost(),getPort());
    }

    public String getHost(){
        return env.getProperty("corgi.rpc.rmi.host","localhost");
    }

    public int getPort(){
        return Integer.parseInt(env.getProperty("corgi.rpc.rmi.port","3333"));
    }
}
