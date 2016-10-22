package com.dounine.corgi.register.api;

import com.dounine.corgi.cluster.ClusterPaths;

import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/22.
 */
public class ZkClusterPaths implements ClusterPaths {

    private ZkRegister register;

    public ZkClusterPaths(ZkRegister register) {
        this.register = register;
    }

    @Override
    public List<String> getPaths(String match) {
        return register.getZkCliInstance().getChildren(match);
    }

    @Override
    public String getNodeData(String match, String node) {
        return register.getZkCliInstance().getData(match + "/" + node);
    }

}
