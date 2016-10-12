package com.dounine.corgi.rpc.serialize;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RequestResult implements Result {
    private Object result;
    private Throwable exception;

    public RequestResult(Object result,Throwable exception){
        this.result = result;
        this.exception = exception;
    }

    @Override
    public Object data() {
        return result;
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
        return "CORGI Result:"+result.toString();
    }
}
