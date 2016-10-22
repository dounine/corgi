package com.dounine.corgi.rpc.protocol;

/**
 * Created by huanghuanlai on 2016/10/18.
 */
public class CorgiProtocol implements IProtocol{

    private String name;
    private int port;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
