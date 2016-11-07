package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.cluster.CirculationBalance;
import com.dounine.corgi.cluster.ClusterPaths;
import com.dounine.corgi.register.*;
import com.dounine.corgi.rpc.RpcApp;
import com.dounine.corgi.rpc.listen.RpcContainer;
import com.dounine.corgi.rpc.protocol.CorgiProtocol;
import com.dounine.corgi.rpc.protocol.IProtocol;
import com.dounine.corgi.spring.rpc.Reference;
import com.dounine.corgi.spring.rpc.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by huanghuanlai on 2016/10/18.
 */
public class SpringProcessor implements BeanPostProcessor,ApplicationListener,IRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringProcessor.class);
    protected static String hostName = null;
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    protected Environment env;
    @org.springframework.beans.factory.annotation.Autowired
    protected Register register;
    @org.springframework.beans.factory.annotation.Autowired
    protected IProtocol protocol;
    @org.springframework.beans.factory.annotation.Autowired
    protected Balance balance;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (checkRpcService(bean)) {
            registerObject(bean);
        }
        reflectProxyReference(bean);
        return bean;
    }

    public boolean checkRpcService(Object bean) {
        return bean.getClass().isAnnotationPresent(Service.class);
    }

    public void reflectProxyReference(Object bean){
        Class<?> cls = bean.getClass();
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

    public void registerObject(Object bean) {
        for (Class interfac : bean.getClass().getInterfaces()) {
            String apiClass = interfac.getName().replace(".","/");
            register.register(new DefaultRegNode(nodeInfo(),"/"+apiClass));
        }
    }

    public String nodeInfo() {
        return hostName+":" + protocol.getPort();
    }

    @Override
    @Bean
    public Register getRegister() {
        String protocol = env.getProperty("corgi.register.protocol","p2p");
        Register reg = getP2PRegister();
        if("zookeeper".equals(protocol)){
            reg = getZkRegister();
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
            RpcApp.init(getProtocol(),env.getProperty("corgi.application.name")).export();
        }
    }

    public boolean exportRpcApp(){
        return true;
    }




}
