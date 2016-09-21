package com.dounine.corgi.rpc.http;


import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.rep.Response;
import com.dounine.corgi.rpc.http.types.HeartType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by huanghuanlai on 16/7/6.
 * 手电筒
 */
public class FlashLight implements IFlashLight{

    protected ICoordinate coordinate;

    protected IRransmission rransmission;

    protected HeartType heartType = HeartType.GET;

    protected IMedia media;

    protected List<IMedia> medias;

    public FlashLight(ICoordinate coordinate){
        setCoordinate(coordinate);
    }

    public FlashLight(){
    }

    public void setCoordinate(ICoordinate coordinate){
        this.coordinate =coordinate;
    }

    /**
     * on of flashlight
     */
    @Override
    public void on(){
        coordinate.init();
    }

    /**
     * off of flashlight
     */
    @Override
    public void off(){

        URLConnection connection = coordinate.point();
        if(connection instanceof HttpURLConnection){
            ((HttpURLConnection)connection).disconnect();
        }
    }

    @Override
    public void emit() throws IOException {
        if(null!=media){
            rransmission.push(heartType,media);
        }else if(null!=medias){
            rransmission.push(heartType,medias);
        }else{
            rransmission.push(heartType);
        }
    }

    @Override
    public void emit(HeartType heartType) throws IOException {
        this.heartType = heartType;
        emit();
    }

    /**
     * emit the Media
     */
    @Override
    public void ready(IMedia media){
        this.media = media;
        rransmission = new Rransmission(this);
    }

    @Override
    public void ready(List<IMedia> medias) {
        this.medias = medias;
        rransmission = new Rransmission(this);
    }

    @Override
    public void irising() {

    }

    @Override
    public ICoordinate coordinate() {
        return coordinate;
    }

    @Override
    public Response response() {
        return rransmission.response();
    }

}
