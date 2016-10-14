package com.dounine.corgi.mongo.dto;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lgq on 16-9-2.
 */
public class BaseDto extends PageDto implements Serializable {
    private static final long serialVersionUID = -3558525794931360478L;
    private List<String> sort; //排序字段
    private String order = "desc"; //排序方式
    private List<SearchJson> searchJsons = new ArrayList<SearchJson>(0);// 类搜索条件
    private Criteria criteria = null; //添加自定义条件


    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<SearchJson> getSearchJsons() {
        return searchJsons;
    }

    public void setSearchJsons(List<SearchJson> searchJsons) {
        this.searchJsons = searchJsons;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
}
