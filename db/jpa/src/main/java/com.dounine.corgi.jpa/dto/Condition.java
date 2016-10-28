package com.dounine.corgi.jpa.dto;


import com.dounine.corgi.jpa.enums.DataType;
import com.dounine.corgi.jpa.enums.RestrictionType;

/**
 * Created by lgq on 16-9-29.
 */
public class Condition {
    private RestrictionType restrict =RestrictionType.EQ; //查询条件 eq gt lt ...
    private String field; //字段
    private String values[];//字段值
    private DataType fieldType = DataType.STRING; //string int ...

    public Condition(){}

    public Condition(String field,DataType fieldType){
        this.field = field;
        this.fieldType =fieldType;
    }
    public Condition(String field,DataType fieldType,String value){
        this.field = field;
        this.fieldType =fieldType;
        values = new String[]{value};
    }

    public RestrictionType getRestrict() {
        return restrict;
    }

    public void setRestrict(RestrictionType restrict) {
        this.restrict = restrict;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public DataType getFieldType() {
        return fieldType;
    }

    public void setFieldType(DataType fieldType) {
        this.fieldType = fieldType;
    }
}
