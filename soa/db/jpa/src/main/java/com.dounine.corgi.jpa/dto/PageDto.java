package com.dounine.corgi.jpa.dto;

/**
 * Created by lgq on 16-9-2.
 */
public class PageDto {
    private int limit = 10;//每显示数量
    private int page = 1;//当前页

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return this.page = this.page-1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
