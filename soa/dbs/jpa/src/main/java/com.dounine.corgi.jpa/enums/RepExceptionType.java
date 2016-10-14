package com.dounine.corgi.jpa.enums;

/**
 * Created by lgq on 16-10-7.
 */
public enum RepExceptionType {
    UNDEFINE, //未定义错误
    NOT_FIND_FIELD, //类属性不存在
    ERROR_ARGUMENTS, //参数数量错误
    ERROR_PARSE_DATE,//时间类型参数转换错误
    ERROR_NUMBER_FORMAT,//字符串转换整形在错误
}
