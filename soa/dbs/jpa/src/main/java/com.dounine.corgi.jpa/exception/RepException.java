package com.dounine.corgi.jpa.exception;


import com.dounine.corgi.jpa.enums.RepExceptionType;

/**
 * Created by lgq on 16-10-7.
 */
public class RepException extends RuntimeException{
    private RepExceptionType type=RepExceptionType.UNDEFINE;

    private com.dounine.corgi.exception.RepException repException;

    public RepException(RepExceptionType repExceptionType,String msg){
        super(msg);
        this.type = repExceptionType;
    }

    public com.dounine.corgi.exception.RepException getRepException() {
        return repException;
    }

    public void setRepException(com.dounine.corgi.exception.RepException repException) {
        this.repException = repException;
    }

    public RepExceptionType getType() {
        return type;
    }

    public void setType(RepExceptionType type) {
        this.type = type;
    }

}
