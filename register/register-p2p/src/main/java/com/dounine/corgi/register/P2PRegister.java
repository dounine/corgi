package com.dounine.corgi.register;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by huanghuanlai on 2016/10/20.
 */
public class P2PRegister implements Register {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PRegister.class);
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();

    private String address;
    private int timeout;
    private Set<Class<?>> registerClass = new HashSet<>();

//    @Override
//    public void register(RegNode regNode) {
//        if(!REGISTER_API_INTERFACES.contains(regNode.getPath())){//filter repeat path
//            REGISTER_API_INTERFACES.add(regNode.getPath());
//            LOGGER.info("CORGI provider api { name : '" + regNode.getPath() + "' }");
//        }
//    }

    @Override
    public void register(Class<?> clazz, String nodeInfo) {
        registerClass.add(clazz);
        LOGGER.info(" > > > ");
        LOGGER.info("CORGI provider scan [ " + clazz + " ]");
        for (Class interfac : clazz.getInterfaces()) {
            String apiClass = interfac.getName().replace(".","/");
            if(!REGISTER_API_INTERFACES.contains(apiClass)){//filter repeat path
                REGISTER_API_INTERFACES.add(apiClass);
                LOGGER.info("CORGI provider register api [ " + interfac + " ]");
            }
        }
        LOGGER.info(" < < < ");
    }

    @Override
    public Set<Class<?>> getRegisterClass() {
        return registerClass;
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

    public List<String> getPaths() {
        return Arrays.asList(address);
    }
}
