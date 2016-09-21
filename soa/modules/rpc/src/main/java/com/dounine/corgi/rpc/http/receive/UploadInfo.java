package com.dounine.corgi.rpc.http.receive;

/**
 * Created by huanghuanlai on 16/6/29.
 */
public class UploadInfo {

    private String token;
    private int shar;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getShar() {
        return shar;
    }

    public void setShar(int shar) {
        this.shar = shar;
    }
}
