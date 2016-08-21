package com.dounine.corgi.rpc.http.rep;

/**
 * Created by huanghuanlai on 16/4/1.
 */
public class ResponseText<T> {
    public static final int SUCCESS_CODE = 0;

    private int errno = SUCCESS_CODE;
    private T data;
    private String msg;

    public ResponseText(){}
    public ResponseText(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
