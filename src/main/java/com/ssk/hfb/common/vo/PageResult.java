package com.ssk.hfb.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private Long total;// 总条数
    private int page; //当前页
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }


    public PageResult(Long total, int page, List<T> items) {
        this.total = total;
        this.page= page;
        this.items = items;
    }
    public PageResult(Long total, Long totalPage, int page, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.page= page;
        this.items = items;
    }

}