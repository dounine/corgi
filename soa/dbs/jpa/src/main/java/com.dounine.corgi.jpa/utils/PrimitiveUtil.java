package com.dounine.corgi.jpa.utils;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.dounine.corgi.jpa.enums.DataType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by lgq on 16-10-6.
 * java 基础类型转换工具类
 */
public class PrimitiveUtil {
    public static final Class[] PRIMITIVES = new Class[]{
            String.class,
            Integer.class,
            Long.class,
            Char.class,
            Double.class,
            Float.class,
            Boolean.class,
            LocalDate.class,
            LocalDateTime.class,
            LocalTime.class
    };

    public static Class switchType(DataType type) {
        Class clazz = null;
        for(Class zz :PRIMITIVES){
            if((type.name()).equalsIgnoreCase(zz.getSimpleName())){
                clazz = zz;
                break;
            }
        }
        clazz = clazz==null?String.class:clazz;
        return clazz;
    }



    /**
     * 类型转换成对应类型
     * @param type
     * @param values
     * @return
     */
    public static Object[] convertValuesByType(String[] values,DataType type){
        int values_length=values.length;
        Object[] result = new Object[values_length];
        for(int i=0;i<values_length;i++){
            switch (type){
                case STRING:result[i] = values[i]; break;
                case INT:result[i] = Integer.parseInt(values[i]); break;
                case FLOAT:result[i] = Float.parseFloat(values[i]); break;
                case CHAR:result[i] = values[i].charAt(0); break;
                case DOUBLE:result[i] = Double.parseDouble(values[i]); break;
                case BOOLEAN:result[i] = Integer.parseInt(values[i]); break;
                case LONG:result[i] = Long.parseLong(values[i]); break;
                case LOCALDATE:result[i] = LocalDate.parse(values[i]); break;
                case LOCALTIME:result[i] = LocalTime.parse(values[i]); break;
                case LOCALDATETIME:result[i] = LocalDateTime.parse(values[i]); break;
                default:
                    result[i] = values[i];
            }
        }

        return result;
    }

}
