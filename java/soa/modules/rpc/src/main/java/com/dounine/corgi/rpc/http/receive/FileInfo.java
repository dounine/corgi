package com.dounine.corgi.rpc.http.receive;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/6/27.
 */
public class FileInfo {

    private String name;
    /**
     * 多少块文件分片
     */
    private int maxShar = 1;
    /**
     * 文件上传事件token
     */
    private String token;

    /**
     * 文件保存的位置
     */
    private String savePath;

    /**
     * 文件上传时间
     */
    private LocalDateTime createTime;

    /**
     * 更新上传时间
     */
    private LocalDateTime updateTime;

    /**
     * 平均上传块的大小(byte)
     */
    private Integer avgShar;
    /**
     * 当前传输的块
     */
    private Integer upShar;
    /**
     * 文件大小 byte
     */
    private Long size;
    private Progress progress;
    /**
     * 已经传输完成的块
     */
    private Integer[] shars = new Integer[0];

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getUpShar() {
        return upShar;
    }

    public void setUpShar(Integer upShar) {
        this.upShar = upShar;
    }

    public Integer[] getShars() {
        return shars;
    }

    public void setShars(Integer[] shars) {
        this.shars = shars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxShar() {
        return maxShar;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setMaxShar(int maxShar) {
        this.maxShar = maxShar;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Integer getAvgShar() {
        return avgShar;
    }

    public void setAvgShar(Integer avgShar) {
        this.avgShar = avgShar;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
