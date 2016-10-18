package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.RPC;
import com.dounine.corgi.rpc.spring.annotation.Reference;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Created by huanghuanlai on 16/10/10.
 */
public class RpcConsumer extends RpcConPro implements BeanPostProcessor,IRpcConPro {

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
                    field.set(bean, RPC.getProxy(field.getType(),field.getAnnotation(Reference.class).version(),register));
                    field.setAccessible(oldAcc);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
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
