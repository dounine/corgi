package com.dounine.corgi.rpc.invoke.config;

import com.dounine.corgi.rpc.zookeeper.Client;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class Provider {

    private String target;
    private int port;
    private boolean useZK = true;
    private Client client;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        if (isUseZK()) {
            this.target = target;
            client = new Client(target);
        }
    }

    public void register(Class clazz) {
        if (isUseZK() && null != client) {
            client.createPersistent("/rpc/" + clazz.getName().replaceAll("\\.", "/"));
            client.createEpseq("/rpc/" + clazz.getName().replaceAll("\\.", "/") + "/node", getNodeInfo());
        }
        System.out.println(clazz.getName() + " => 发布成功");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNodeInfo() {
        try {
            return "http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + getPort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUseZK() {
        return useZK;
    }

    public void unUseZK() {
        this.useZK = false;
    }
}
