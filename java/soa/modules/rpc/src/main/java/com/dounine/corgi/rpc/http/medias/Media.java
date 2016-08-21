package com.dounine.corgi.rpc.http.medias;

/**
 * Created by huanghuanlai on 16/7/6.
 * 介质
 */
public class Media implements IMedia{

    protected String name;
    protected String value;

    public Media(){}
    public Media(String name,Object value){
        this.name = name;
        this.value = value.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
