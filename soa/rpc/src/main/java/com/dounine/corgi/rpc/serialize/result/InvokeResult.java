package com.dounine.corgi.rpc.serialize.result;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class InvokeResult implements IResult {
    private Object data;
    private Throwable exception;

    public InvokeResult(){}

    public InvokeResult(Object data, Throwable exception) {
        this.data = data;
        this.exception = exception;
    }

    @Override
    public Object result() throws Throwable {
        if(this.hasException()){
            throw this.exception();
        }
        return data;
    }

    @Override
    public Throwable exception() {
        return exception;
    }

    @Override
    public boolean hasException() {
        return null != exception;
    }

    @Override
    public String toString() {
        return "CORGI Result:" + data.toString();
    }
}
