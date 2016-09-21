package com.dounine.corgi.rpc.http.medias;

import java.io.File;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public class MediaFile extends Media implements IMedia,IMediaFile {

    protected File file;

    public MediaFile(){}
    public MediaFile(File file){
        this.file = file;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }
}
