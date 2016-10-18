package com.dounine.corgi.rpc.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * Created by huanghuanlai on 16/7/28.
 */
public class ZkClient {
    private org.I0Itec.zkclient.ZkClient zkClient;
    private volatile Watcher.Event.KeeperState state = Watcher.Event.KeeperState.SyncConnected;

    public ZkClient(String url) {
        zkClient = new org.I0Itec.zkclient.ZkClient(url);
    }

    public void createPersistent(String path) {
        zkClient.createPersistent(path, true);
    }

    public void createEpseq(String path, Object data) {
        zkClient.createEphemeralSequential(path, data);
    }

    public List<String> getChildren(String path) {
        try {
            List<String> pathList = zkClient.getChildren(path);
            if (pathList != null && pathList.size() > 0) {
                return pathList;
            }
        } catch (ZkNoNodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getData(String path) {
        try {
            return zkClient.readData(path, true);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    public void delete(String path) {
        try {
            zkClient.delete(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    public void setWatcher(String path, IZkDataListener watcher){
        zkClient.subscribeDataChanges(path,watcher);
    }

    public boolean isConnected(){
        return state== Watcher.Event.KeeperState.SyncConnected;
    }

    public void close(){
        zkClient.close();
    }
}
