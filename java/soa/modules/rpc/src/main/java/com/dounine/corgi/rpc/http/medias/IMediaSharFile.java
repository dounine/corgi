package com.dounine.corgi.rpc.http.medias;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public interface IMediaSharFile extends IMediaFile {

    void setSharSize(long sharSize);

    void setToken(String token);

    long getSharSize();

    String getToken();

}
