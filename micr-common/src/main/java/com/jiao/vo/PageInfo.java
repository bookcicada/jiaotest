package com.jiao.vo;

/**
 * vo:value object
 *
 * @author 18067
 * @Date 2021/9/14 21:08
 */
public class PageInfo {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalRecords;
    //totalRecords总记录数
    //totalPages总页数

    public PageInfo(Integer pageNo, Integer pageSize, Integer totalRecords) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        //totalRecords总记录数
        //totalPages总页数
        if (totalRecords % pageSize == 0) {
            totalPages = totalRecords / pageSize;
        } else {
            totalPages = totalRecords / pageSize + 1;
        }
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }



    public PageInfo() {
        pageNo = 1;
        pageSize = 9;
        totalPages = 0;
        totalRecords = 0;
    }
}
