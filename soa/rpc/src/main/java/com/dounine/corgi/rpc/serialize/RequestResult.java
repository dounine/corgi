package com.dounine.corgi.rpc.serialize;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RequestResult implements Result {
    private Object data;
    private Throwable exception;

    public RequestResult(Object data,Throwable exception){
        this.data = data;
        this.exception = exception;
    }

    @Override
    public Object data() {
        return data;
    }

    @Override
    public Throwable exception() {
        return exception;
    }

    @Override
    public boolean hasError() {
        return null!=exception;
    }

    @Override
    public String toString() {
        return "CORGI Result:"+data.toString();
    }
}
