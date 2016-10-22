package com.dounine.corgi.mongo.enums;

/**
 * Created by lgq on 16-10-7.
 */
public enum DataType {
    STRING(0),
    INT(1),
    FLOAT(2),
    DOUBLE(3),
    BOOLEAN(4),
    LONG(5),
    LOCALDATE(6),
    LOCALTIME(7),
    LOCALDATETIME(8);
    private int code;

    DataType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
