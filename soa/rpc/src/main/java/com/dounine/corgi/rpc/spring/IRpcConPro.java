package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.invoke.config.IProtocol;
import com.dounine.corgi.rpc.invoke.config.IRegister;
import org.springframework.context.annotation.Bean;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public interface IRpcConPro {

    default String getType(){
        return null;
    }

    default String getHost(){
        return null;
    }

    default int getPort(){
        return 3333;
    }

    @Bean
    IRegister getRegister();

    @Bean
    IProtocol getProtocol();

}
