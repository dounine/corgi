package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.cluster.CirculationBalance;
import com.dounine.corgi.cluster.ClusterPaths;
import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.register.*;
import com.dounine.corgi.rpc.RpcApp;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.rpc.protocol.CorgiProtocol;
import com.dounine.corgi.rpc.protocol.IProtocol;
import com.dounine.corgi.spring.rpc.Reference;
import com.dounine.corgi.spring.rpc.Service;
import com.dounine.corgi.utils.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by huanghuanlai on 2016/10/18.
 */
public class SpringProcessor implements BeanPostProcessor,ApplicationListener,IRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringProcessor.class);
    protected static String hostName = null;
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();
    private static final Set<Class<?>> PROVIDER_CLASS = new HashSet<>();

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    protected Environment env;
    @Autowired
    protected Register register;
    @Autowired
    protected IProtocol protocol;
    @Autowired
    protected Balance balance;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        reflectProxyReference(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> originClass = bean.getClass();
        if (checkRpcService(originClass)) {
            register.register(originClass,nodeInfo());
        }
        return bean;
    }

    public boolean checkRpcService(Class<?> clazz) {
        return clazz.isAnnotationPresent(Service.class);
    }

    public void reflectProxyReference(Object bean){
        Class<?> cls = ProxyUtils.getOriginClass(bean.getClass());
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Reference.class)) {
                boolean oldAcc = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(bean, RpcApp.instance().getProxy(field.getType(),field.getAnnotation(Reference.class),balance));
                    field.setAccessible(oldAcc);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String nodeInfo() {
        return hostName+":" + protocol.getPort();
    }

    @Override
    @Bean
    public Register getRegister() {
        String protocol = env.getProperty("corgi.register.protocol","p2p");
        Register reg = null;
        if("p2p".equals(protocol)){
            reg = getP2PRegister();
        }else if("zookeeper".equals(protocol)){
            reg = getZkRegister();
        }else{
            throw new RPCException("CORGI rpc protocol not match.");
        }
        return reg;
    }

    public ZkRegister getZkRegister(){
        ZkRegister registerConfig = new ZkRegister();
        registerConfig.setAddress(env.getProperty("corgi.register.address","localhost:2181"));
        registerConfig.setTimeout(env.getProperty("corgi.register.timeout",Integer.class,3000));
        return registerConfig;
    }

    public P2PRegister getP2PRegister(){
        P2PRegister registerConfig = new P2PRegister();
        registerConfig.setAddress(env.getProperty("corgi.register.address","localhost:7777"));
        registerConfig.setTimeout(env.getProperty("corgi.register.timeout",Integer.class,3000));
        return registerConfig;
    }

    @Override
    @Bean
    public Balance getBalance() {
        String protocol = env.getProperty("corgi.register.protocol","p2p");
        ClusterPaths paths = null;
        if("zookeeper".equals(protocol)){
            paths = new ZkClusterPaths(getZkRegister());
        }else if("p2p".equals(protocol)){
            paths = new P2PClusterPaths(getP2PRegister());
        }
        return new CirculationBalance(paths);
    }

    @Override
    @Bean
    public IProtocol getProtocol(){
        CorgiProtocol corgiProtocol = new CorgiProtocol();
        corgiProtocol.setName(env.getProperty("corgi.protocol.name","corgi"));
        corgiProtocol.setPort(env.getProperty("corgi.protocol.port",Integer.class,7777));
        return corgiProtocol;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(!RpcContainer.isListener()&&exportRpcApp()){
            RpcApp.init(protocol,register,env.getProperty("corgi.application.name")).export();
        }
    }

    public boolean exportRpcApp(){
        return true;
    }

}
