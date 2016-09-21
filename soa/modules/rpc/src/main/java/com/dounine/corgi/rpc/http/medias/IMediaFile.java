package com.dounine.corgi.rpc.http.medias;

import java.io.File;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public interface IMediaFile extends IMedia {

    File getFile();

    void setFile(File file);

}
