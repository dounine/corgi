package com.dounine.corgi.rpc.http.rep;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public class Response {

    protected int code;
    protected String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        if(null==text){
            return new StringBuilder("{")
                    .append("code:")
                    .append(code)
                    .append("}").toString();
        }
        return new StringBuilder("{")
                .append("code:")
                .append(code)
                .append(",text:")
                .append("\"")
                .append(text)
                .append("\"")
                .append("}").toString();
    }
}
