package com.dounine.corgi.rpc.invoke.config;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.rpc.zk.ZkClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class RegisterConfig implements IRegister {

    private int port;
    private String host;
    private String type;
    private ZkClient client;
    private static final Map<Class, AtomicInteger> invokeCount = new ConcurrentHashMap<>();

    public ZkClient getClient() {
        if (null == this.client) {
            this.client = new ZkClient(getHost() + ":" + getPort());
        }
        return this.client;
    }

    @Override
    public NodeInfo getNodeInfo(Class clazz) {
        String hostAndPort = getBalancedUrl(clazz);
        if (StringUtils.isNotBlank(hostAndPort)) {
            String[] hp = hostAndPort.split(":");
            return new NodeInfo(hp[0], Integer.parseInt(hp[1]));
        }
        throw new RPCException("CORGI rpc not provider nodeinfo.");
    }

    @Override
    public List<String> getHostPorts(Class clazz) {
        List<String> hostPorts = new ArrayList<>();
        List<String> pathList = getClient().getChildren("/" + clazz.getName().replace(".", "/"));
        if (null != pathList) {
            for (String path : pathList) {
                String httpUrl = getClient().getData("/" + clazz.getName().replace(".", "/") + "/" + path);
                if (null != httpUrl) {
                    hostPorts.add(httpUrl);
                }
            }
        }
        if (hostPorts.size() == 0) {
            throw new RPCException("CORGI rpc not provider.");
        }
        return hostPorts;
    }

    @Override
    public String getBalancedUrl(Class clazz) {
        List<String> hostPorts = getHostPorts(clazz);
        AtomicInteger count = invokeCount.get(clazz);
        if (null == count) {
            invokeCount.put(clazz, new AtomicInteger(1));
            return hostPorts.get(0);
        } else {
            if (count.get() == Integer.MAX_VALUE >> 1) {
                invokeCount.put(clazz, new AtomicInteger(1));
                count = invokeCount.get(clazz);
            }
        }
        return hostPorts.get(count.getAndIncrement() % hostPorts.size());
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
