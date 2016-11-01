package com.dounine.corgi.register;

/**
 * Created by huanghuanlai on 2016/10/22.
 */
public class DefaultRegNode implements RegNode {
    private String address;
    private String path;

    public DefaultRegNode(String address,String path){
        this.address = address;
        this.path = path;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
