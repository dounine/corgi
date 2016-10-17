package com.dounine.corgi.rpc.invoke.config;

import com.dounine.corgi.rpc.zk.ZkClient;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class ProtocolConfig implements IProtocol{

    private int port;
    private ZkClient client;
    private String type;

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setType(String type) {
        this.type = type;
    }
}
