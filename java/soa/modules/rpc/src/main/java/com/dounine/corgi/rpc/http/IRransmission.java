package com.dounine.corgi.rpc.http;

import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.rep.Response;
import com.dounine.corgi.rpc.http.types.HeartType;

import java.io.IOException;
import java.util.List;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public interface IRransmission {

    void setFlashLight(IFlashLight flashLight);

    void heartbeat(HeartType heartType) throws IOException;

    void push(HeartType heartType) throws IOException;

    void push(HeartType heartType, IMedia media) throws IOException;

    void push(HeartType heartType, List<IMedia> medias) throws IOException;

    Response response();
}
