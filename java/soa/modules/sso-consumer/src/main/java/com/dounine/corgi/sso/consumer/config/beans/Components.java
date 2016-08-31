package com.dounine.corgi.sso.consumer.config.beans;

import com.dounine.corgi.rpc.invoke.config.Consumer;
import com.dounine.corgi.rpc.proxy.ConsumerProxyFactory;
import com.dounine.corgi.sso.service.user.IUserSer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 16/8/17.
 */
@Component
public class Components {
    @Bean
    public IUserSer getUserSer(){
        return consumerProxyFactory().create(IUserSer.class);
    }

    @Bean
    public ConsumerProxyFactory consumerProxyFactory(){
        ConsumerProxyFactory consumerProxyFactory = new ConsumerProxyFactory();
        Consumer consumer = new Consumer();
        consumer.unUseZK();//不使用zookeeper作为服务路由
        consumer.setUrl("127.0.0.1:7777");
        consumerProxyFactory.setClassName("com.dounine.corgi.sso.service.user.IUserSer");
        consumerProxyFactory.setConsumer(consumer);
        return consumerProxyFactory;
    }
}
