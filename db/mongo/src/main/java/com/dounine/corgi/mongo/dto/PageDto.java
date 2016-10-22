package com.dounine.corgi.mongo.dto;

/**
 * Created by lgq on 16-9-2.
 */
public class PageDto {
    private Integer skip = 0;//起始记录下标
    private Integer limit = 10;//每显示数量
    private Integer page = 1;//当前页

    public Integer getSkip() {
       this.skip = (this.page - 1) * limit;
       return skip ;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }
}
