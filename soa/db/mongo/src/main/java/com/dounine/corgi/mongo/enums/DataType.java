package com.dounine.corgi.mongo.enums;

/**
 * Created by lgq on 16-10-7.
 */
public enum DataType {
    STRING(0),
    INT(1),
    FLOAT(2),
    CHAR(3),
    DOUBLE(4),
    BOOLEAN(5),
    LONG(6),
    LOCALDATE(7),
    LOCALTIME(8),
    LOCALDATETIME(9);
    private int code;

    DataType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
