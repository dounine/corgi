package com.dounine.corgi.register;

/**
 * Created by huanghuanlai on 2016/10/21.
 */
public class ZkRegNode implements RegNode {

    private String path;
    private String address;

    public ZkRegNode(String path,String address){
        this.path = path;
        this.address = address;
    }


    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
