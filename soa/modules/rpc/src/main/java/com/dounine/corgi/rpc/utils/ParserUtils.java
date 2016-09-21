package com.dounine.corgi.rpc.utils;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

/**
 * Created by huanghuanlai on 16/8/18.
 */
public class ParserUtils {

    public static Object parseObject(Object data, Class<?> type) {
        if (type == String.class) {
            return data;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(data.toString());
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(data.toString());
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(data.toString());
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(data.toString());
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(data.toString());
        } else if (type == Short.class || type == short.class) {
            return Short.parseShort(data.toString());
        } else if (type == char.class) {
            return data.toString().toCharArray()[0];
        } else if (type == BigDecimal.class) {
            return BigDecimal.valueOf(Long.parseLong(data.toString()));
        }
        return JSON.parseObject(data.toString(), type);
    }
}
