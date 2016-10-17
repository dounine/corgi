package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.invoke.config.RegisterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public abstract class RpcConPro implements IRpcConPro {

    @Autowired
    protected Environment env;
    @Autowired
    protected IRegister register;

    @Bean
    public IRegister getRegister(){
        String protocol = env.getProperty("corgi.rpc.register");
        String[] ps = protocol.split("://");
        String[] hp = ps[1].split(":");
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setType(ps[0]);
        registerConfig.setPort(Integer.parseInt(hp[1]));
        registerConfig.setHost(hp[0]);
        return registerConfig;
    }

}
