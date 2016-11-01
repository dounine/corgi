package com.dounine.corgi.register;

import com.dounine.corgi.cluster.ClusterPaths;

import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/22.
 */
public class P2PClusterPaths implements ClusterPaths {

    private P2PRegister register;

    public P2PClusterPaths(P2PRegister register){
        this.register = register;
    }

    @Override
    public List<String> getPaths(String match) {
        return register.getPaths();
    }

    @Override
    public String getNodeData(String match, String node) {
        return node;
    }

}
