package com.dounine.corgi.jpa.enums;

/**
 * Created by lgq on 16-10-7.
 */
public enum Status {
    THAW(0)//解冻(正常)
    , CONGEAL(1)//冻结
    , DELETE(2)//删除
    , NOACTIVE(3)//未激活
    , UNREVIEW(4)//未审核
    ;

    private int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
