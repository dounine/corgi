package com.dounine.corgi.rpc.invoke;


import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.medias.Media;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class FetchData extends Media implements IMedia {

    public FetchData(){}
    public FetchData(String name,Object value){
        this.name = name;
        this.value = value.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setV(Object value) {
        this.value = value.toString();
    }
    public Object getV(){
        return value;
    }
}
