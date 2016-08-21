package com.dounine.corgi.rpc.invoke.config;

import com.dounine.corgi.rpc.zookeeper.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class Consumer {
    private String url;
    private Client client;
    private boolean useZK = true;
    private final Map<Class, AtomicInteger> invokeCount = new ConcurrentHashMap<>();

    public String getUrl(Class clazz) {
        if (isUseZK() && null != client) {
            List<String> urls = new ArrayList<>();
            List<String> pathList = client.getChildren("/rpc/" + clazz.getName().replaceAll("\\.", "/"));
            if (null != pathList) {
                for (String path : pathList) {
                    String httpUrl = client.getData("/rpc/" + clazz.getName().replaceAll("\\.", "/") + "/" + path);
                    if (null != httpUrl) {
                        urls.add(httpUrl);
                    }
                }
            }
            if (!urls.isEmpty()) {
                return getBalancedUrl(clazz, urls);
            }
        } else {
            return url;
        }
        throw new RuntimeException("没有服务提供方");
    }

    /**
     * 获取负载均衡地扯
     *
     * @param clazz 调用接口类
     * @param urls  所有调用地扯
     * @return 均衡地扯
     */
    public String getBalancedUrl(Class clazz, List<String> urls) {
        AtomicInteger count = invokeCount.get(clazz);
        if (null == count) {
            invokeCount.put(clazz, new AtomicInteger(1));
            return urls.get(0);
        } else {
            if (count.get() == Integer.MAX_VALUE) {
                invokeCount.put(clazz, new AtomicInteger(1));
                count = invokeCount.get(clazz);
            }
        }
        return urls.get(count.getAndIncrement() % urls.size());
    }

    public void setUrl(String url) {
        if (isUseZK()) {
            client = new Client(url);
        }else{
            if(!url.startsWith("http://")){
                this.url = "http://"+url;
            }else{
                this.url = url;
            }
        }
    }

    public boolean isUseZK() {
        return useZK;
    }

    public void unUseZK() {
        this.useZK = false;
    }
}
