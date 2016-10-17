package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.invoke.config.IRegister;
import org.springframework.context.annotation.Bean;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public interface IRpcConPro {

    String getType();

    String getHost();

    int getPort();

    @Bean
    IRegister getRegister();

}
