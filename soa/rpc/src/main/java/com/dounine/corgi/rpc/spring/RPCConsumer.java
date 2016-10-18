package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.RPC;
import com.dounine.corgi.rpc.spring.annotation.Reference;
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

}
