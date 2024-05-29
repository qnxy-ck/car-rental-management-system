package com.qnxy.common.data;

import java.util.List;

/**
 * 分页信息
 * 
 * @author Qnxy
 */
public class PageInfo<DATA> {

    /**
     * 查询结果数据
     */
    private final List<DATA> records;

    /**
     * 当前页
     */
    private final int currentPage;

    /**
     * 当前页数量
     */
    private final int pageSize;

    /**
     * 总页数
     */
    private final int total;

    public PageInfo(List<DATA> records, int currentPage, int pageSize, int total) {
        this.records = records;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<DATA> getRecords() {
        return records;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }
}
