package com.dounine.corgi.cluster;

import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/22.
 */
public interface ClusterPaths {

    List<String> getPaths(String match);

    String getNodeData(String match,String node);

}
