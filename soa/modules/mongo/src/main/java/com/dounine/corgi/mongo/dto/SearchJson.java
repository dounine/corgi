package com.dounine.corgi.mongo.dto;


import com.dounine.corgi.mongo.enums.RestrictionType;

/**
 * @Package: [ mongo.dto ]
 * @Author: [ huanghuanlai ]
 * @CreateTime: [ 2016年1月19日 下午3:28:35 ]
 * @Copy: [ bjike.com ]
 * @Version: [ v1.0 ]
 * @Description: [ 页面传递搜索信息 ]
 */
public class SearchJson {
    private RestrictionType searchName;
    private String[] searchField;//fieldName filedType value ...

    public RestrictionType getSearchName() {
        return searchName;
    }

    public void setSearchName(RestrictionType searchName) {
        this.searchName = searchName;
    }

    public String[] getSearchField() {
        return searchField;
    }

    public void setSearchField(String[] searchField) {
        this.searchField = searchField;
    }

}
