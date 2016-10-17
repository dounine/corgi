package com.dounine.corgi.rpc.invoke.config;

import com.dounine.corgi.rpc.zk.ZkClient;

import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/17.
 */
public interface IRegister {

    ZkClient getClient();

    NodeInfo getNodeInfo(Class clazz);

    List<String> getHostPorts(Class clazz);

    String getBalancedUrl(Class clazz);
}
