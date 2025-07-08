package com.keensense.densecrowd.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author zengyc
 */

public class AlarmPageUtils implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 当前页数
     */
    private int currentPage;


    /**
     * 总页数
     */
    private int totalPage;


    /**
     * 总记录数
     */
    private int totalRows;

    /**
     * 当页起始行号
     */
    private int minRowNumber;

    /**
     * 当页结束行号
     */
    private int maxRowNumber;

    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public AlarmPageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalRows = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public AlarmPageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalRows = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.currentPage = (int) page.getCurrent();
        this.totalPage = (int) page.getPages();
        this.minRowNumber = (int) page.offset() + 1;
        this.maxRowNumber = ((int) page.offset() + this.pageSize) > this.totalRows ? this.totalRows : ((int) page.offset() + this.pageSize);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getMinRowNumber() {
        return minRowNumber;
    }

    public void setMinRowNumber(int minRowNumber) {
        this.minRowNumber = minRowNumber;
    }

    public int getMaxRowNumber() {
        return maxRowNumber;
    }

    public void setMaxRowNumber(int maxRowNumber) {
        this.maxRowNumber = maxRowNumber;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
