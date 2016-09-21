package com.dounine.corgi.rpc.http.receive;

/**
 * Created by huanghuanlai on 16/6/30.
 */
public class SharSize {

    /**
     * 最小
     */
    private long minSize;
    /**
     * 最大
     */
    private long maxSize;
    /**
     * 分片大小
     */
    private int shar;

    public SharSize(long minSize,long maxSize,int shar){
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.shar = shar;
    }

    public long getMinSize() {
        return minSize;
    }

    public void setMinSize(long minSize) {
        this.minSize = minSize;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public int getShar() {
        return shar;
    }

    public void setShar(int shar) {
        this.shar = shar;
    }
}
