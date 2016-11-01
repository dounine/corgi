package com.dounine.corgi.register;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/20.
 */
public class P2PRegister implements Register {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PRegister.class);
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();

    private String address;
    private int timeout;

    @Override
    public void register(RegNode regNode) {
        if(!REGISTER_API_INTERFACES.contains(regNode.getPath())){//filter repeat path
            REGISTER_API_INTERFACES.add(regNode.getPath());
            LOGGER.info("CORGI provider api { name : '" + regNode.getPath() + "' }");;
        }
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
