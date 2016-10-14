package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.proxy.RPC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

/**
 * Created by huanghuanlai on 16/10/10.
 */
public class RPCConsumer implements BeanPostProcessor{

    @Autowired
    private Environment env;

    public String getHost(){
        return env.getProperty("corgi.rpc.rmi.host","localhost");
    }

    public int getPort(){
        return Integer.parseInt(env.getProperty("corgi.rpc.rmi.port","9999"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> cls = bean.getClass();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Reference.class)) {
                boolean oldAcc = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(bean, RPC.getProxy(field.getType(),getHost(),getPort(),field.getAnnotation(Reference.class).version()));
                    field.setAccessible(oldAcc);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}
