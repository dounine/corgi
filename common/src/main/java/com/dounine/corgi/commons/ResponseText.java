package com.dounine.corgi.commons;

/**
 * Created by huanghuanlai on 16/4/1.
 */
public class ResponseText {

    private int code = 0;
    private Object data;
    private String msg;

    public ResponseText(){}
    public ResponseText(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
