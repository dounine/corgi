package com.dounine.corgi.exception;

import java.io.Serializable;

/**
 * Created by huanghuanlai on 16/8/19.
 */
public class RPCException extends RuntimeException implements Serializable{

    protected String message;

    public RPCException(){}

    public RPCException(String message){
        super(new Throwable(message));
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
