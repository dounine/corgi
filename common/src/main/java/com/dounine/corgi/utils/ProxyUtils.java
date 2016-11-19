package com.dounine.corgi.utils;

import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by huanghuanlai on 2016/11/19.
 */
public final class ProxyUtils {
    private ProxyUtils(){}

    private final static boolean isProxyClass(Object bean){
        if(bean instanceof Proxy){//jdk proxy
            return true;
        }else if(bean instanceof MethodInterceptor){
            return true;
        }
        return false;
    }

    public static Class<?> getOriginClass(Object bean){
        if(isProxyClass(bean)){
            return bean.getClass().getInterfaces()[0];
        }
        Class cc = null;
        if(!(bean instanceof Class)){
            cc = bean.getClass();
        }else{
            cc = (Class<?>) bean;
        }

        String[] classStrs = cc.getName().split("\\$\\$");
        Class zz = null;
        if(null!=classStrs&&classStrs.length>1){
            try {
                zz = Class.forName(classStrs[0]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return zz!=null?zz:cc;
    }

    public static void main(String[] args) {
//        CC cc = (CC) Proxy.newProxyInstance(ProxyUtils.class.getClassLoader(), new Class[]{CC.class}, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                return null;
//            }
//        });
//
//        System.out.println(ProxyUtils.getOriginClass(CC.class));
    }
}
