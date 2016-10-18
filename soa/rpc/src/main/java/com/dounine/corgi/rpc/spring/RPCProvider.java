package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.rpc.RPC;
import com.dounine.corgi.rpc.invoke.config.IProtocol;
import com.dounine.corgi.rpc.invoke.config.ProtocolConfig;
import com.dounine.corgi.rpc.listen.RpcListener;
import com.dounine.corgi.rpc.spring.annotation.Service;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
public class RpcProvider extends RpcConPro implements ApplicationListener,BeanPostProcessor,IRpcConPro {

    static String hostName = null;
    private static final List<Class> REGISTER_ClASS = new ArrayList<>();

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public IProtocol getProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(getPort());
        protocolConfig.setType(getType());
        return protocolConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!RpcListener.isListener()) {
            RPC.export(getProtocolConfig());
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public boolean checkRpcService(Object bean) {
        return bean.getClass().isAnnotationPresent(Service.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (checkRpcService(bean)) {
            registerObject(bean);
        }
        return bean;
    }

    public void registerObject(Object bean) {
        Class apiClass = null;
        for (Class interfac : bean.getClass().getInterfaces()) {
            if (!REGISTER_ClASS.contains(interfac)) {
                apiClass = interfac;
                break;
            }
        }
        if (null != apiClass) {
            REGISTER_ClASS.add(apiClass);
            String apiPath = apiClass.getName().replace(".", "/");
            register.getClient().createPersistent("/" + apiPath);
            register.getClient().createEpseq("/" + apiPath + "/node", nodeInfo());
            LOGGER.info("CORGI rpc provider { name : '" + apiClass.getName() + "' }");
        }
    }

    public String nodeInfo() {
        return hostName + ":" + getPort();
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

    public String[] getValue() {
        String protocol = env.getProperty("corgi.rpc.protocol");
        String[] ps = protocol.split("://");
        String[] hp = ps[1].split(":");
        return ArrayUtils.add(hp, ps[0]);
    }

}