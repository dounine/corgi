package com.dounine.corgi.cluster;

import com.dounine.corgi.exception.RPCException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huanghuanlai on 2016/10/21.
 */
public class CirculationBalance implements Balance {
    private static final Map<String, AtomicInteger> PATH_MATHS = new ConcurrentHashMap<>();
    private ClusterPaths clusterPaths;

    public CirculationBalance(ClusterPaths clusterPaths){
        this.clusterPaths = clusterPaths;
    }

    @Override
    public String getBalance(String match) {
        List<String> address = clusterPaths.getPaths(match);
        List<String> hostPorts = new ArrayList<>();
        if (null != address) {
            for (String path : address) {
                String httpUrl = clusterPaths.getNodeData(match,path);
                if (null != httpUrl) {
                    hostPorts.add(httpUrl);
                }
            }
        }
        if (hostPorts.size() == 0) {
            throw new RPCException("CORGI rpc not provider.");
        }
        AtomicInteger count = PATH_MATHS.get(match);
        if (null == count) {
            PATH_MATHS.put(match, new AtomicInteger(1));
            return hostPorts.get(0);
        } else {
            if (count.get() == Integer.MAX_VALUE >> 1) {
                PATH_MATHS.put(match, new AtomicInteger(1));
                count = PATH_MATHS.get(match);
            }
        }
        return hostPorts.get(count.getAndIncrement() % hostPorts.size());
    }
}
