package com.dounine.corgi.exception;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by huanghuanlai on 16/8/19.
 */
public class RPCException extends UndeclaredThrowableException {

    protected String message;

    public RPCException(){
        super(null);
    }
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
