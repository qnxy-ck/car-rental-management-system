package com.qnxy.common.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 分页信息
 *
 * @author Qnxy
 */
@Data
@RequiredArgsConstructor
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

    public PageInfo(List<DATA> records, int currentPage, int total) {
        this.records = records;
        this.currentPage = currentPage;
        this.pageSize = records.size();
        this.total = total;
    }

}
