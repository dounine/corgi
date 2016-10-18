package com.dounine.corgi.rpc.invoke.config;

import java.io.Serializable;

/**
 * Created by huanghuanlai on 2016/10/17.
 */
public class NodeInfo implements Serializable{
    private String host;
    private int port;

    public NodeInfo(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
