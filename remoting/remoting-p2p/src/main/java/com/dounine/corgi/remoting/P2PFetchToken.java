package com.dounine.corgi.remoting;

import java.io.Serializable;

/**
 * Created by huanghuanlai on 2016/11/7.
 */
public class P2PFetchToken implements FetchToken,Serializable {

    private String address;
    private String token;
    private int timeout;
    private boolean txCommit;
    private int retries;

    public P2PFetchToken(String token,int timeout,int retries,String address,boolean txCommit){
        this.token = token;
        this.timeout = timeout;
        this.retries = retries;
        this.address = address;
        this.txCommit = txCommit;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public boolean checkCommit() {
        return txCommit;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
