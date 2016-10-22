package com.dounine.corgi.mongo.dto;

import com.dounine.corgi.mongo.enums.DataType;
import com.dounine.corgi.mongo.enums.RestrictionType;

/**
 * Created by lgq on 16-10-17.
 */
public class Condition {

    public Condition() {
    }

    public Condition(String field, DataType fieldType) {
        this.field = field;
        this.fieldType = fieldType;
    }

    private RestrictionType restrict; //查询条件 eq gt lt ...
    private String field; //字段
    private String values[];//字段值
    private DataType fieldType = DataType.STRING; //string int double ...

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
