package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.invoke.config.IProtocol;
import com.dounine.corgi.rpc.invoke.config.IRegister;
import com.dounine.corgi.rpc.invoke.config.RegisterConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class RpcConPro implements IRpcConPro {

    @Autowired
    protected Environment env;
    @Autowired
    protected IRegister register;
    @Autowired
    protected IProtocol protocol;

    @Bean
    public IRegister getRegister(){
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setType(getType());
        registerConfig.setPort(getPort());
        registerConfig.setHost(getHost());
        return registerConfig;
    }

    @Bean
    public IProtocol getProtocol() {
        return null;
    }

    @Override
    public String getType() {
        return getValue()[2];
    }

    @Override
    public String getHost() {
        return getValue()[0];
    }

    @Override
    public int getPort() {
        return Integer.parseInt(getValue()[1]);
    }

    public String[] getValue(){
        String protocol = env.getProperty("corgi.rpc.register");
        String[] ps = protocol.split("://");
        String[] hp = ps[1].split(":");
        return ArrayUtils.add(hp,ps[0]);
    }

}
