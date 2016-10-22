package com.dounine.corgi.register.api;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/20.
 */
public class ZkRegister implements Register {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegister.class);
    private static final List<String> REGISTER_API_INTERFACES = new ArrayList<>();

    private String address;
    private ZkClient client;
    private int timeout;

    @Override
    public void register(RegNode regNode) {
        if(!REGISTER_API_INTERFACES.contains(regNode.getPath())){//filter repeat path
            ZkClient instanceClient = getZkCliInstance();
            instanceClient.createPersistent(regNode.getPath());
            instanceClient.createEpseq(regNode.getPath()+"/node",regNode.getAddress());
            LOGGER.info("CORGI provider api { name : '" + regNode.getPath() + "' }");;
        }
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
