package com.dounine.corgi.register;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huanghuanlai on 2016/10/20.
 */
public class ZkRegister implements Register {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegister.class);
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();

    private String address;
    private ZkClient client;
    private int timeout;
    private Set<Class<?>> registerClass = new HashSet<>();

    @Override
    public void register(Class<?> clazz,String nodeInfo) {
        registerClass.add(clazz);
        LOGGER.info("{{--------------------------------");
        LOGGER.info("CORGI provider scan [ " + clazz + " ]");
        for (Class interfac : clazz.getInterfaces()) {
            String apiClass = interfac.getName().replace(".","/");
            if(!REGISTER_API_INTERFACES.contains(apiClass)){//filter repeat path
                ZkClient instanceClient = getZkCliInstance();
                instanceClient.createPersistent(apiClass);
                instanceClient.createEpseq(apiClass+"/node",nodeInfo);
                LOGGER.info("CORGI provider register api [ " + interfac + " ]");;
            }
        }
        LOGGER.info("-------------------------------}}");
    }

    @Override
    public Set<Class<?>> getRegisterClass() {
        return registerClass;
    }

    public ZkClient getZkCliInstance(){
        if(null==client){
            this.client = new ZkClient(address,3000);
        }
        return this.client;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
