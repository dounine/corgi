package com.dounine.corgi.rpc.http.receive;

/**
 * Created by huanghuanlai on 16/6/29.
 */
public class Progress {

    private String token;
    private long upSize;
    private int percent;

    public Progress(){}

    public Progress(String token,int percent){
        this.token = token;
        this.percent = percent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public long getUpSize() {
        return upSize;
    }

    public void setUpSize(long upSize) {
        this.upSize = upSize;
    }
}
