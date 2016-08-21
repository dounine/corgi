package com.dounine.corgi.rpc.http.medias;

import java.io.File;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public class MediaSharFile extends MediaFile implements IMediaSharFile {

    protected long sharSize;
    protected String token;

    public MediaSharFile(File file){
        this.file = file;
    }

    @Override
    public void setSharSize(long sharSize) {
        this.sharSize = sharSize;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public long getSharSize() {
        return sharSize;
    }

    @Override
    public String getToken() {
        return token;
    }
}
