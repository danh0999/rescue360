package com.example.prm392_project.data.external.response;

public class PaginationReq {
    private int page;
    private int pageSize;

    public PaginationReq(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}