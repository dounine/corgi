package com.dounine.corgi.rpc.http;

import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.rep.Response;
import com.dounine.corgi.rpc.http.types.HeartType;

import java.io.IOException;
import java.util.List;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public interface IFlashLight {

    void on();

    void off();

    void emit() throws IOException;

    void emit(HeartType heartType) throws IOException;

    void ready(IMedia media);

    void ready(List<IMedia> medias);

    void irising();

    ICoordinate coordinate();

    Response response();
}
