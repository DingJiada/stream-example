package com.shouzhi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shouzhi.pojo.db.LogLogin;

import java.io.Serializable;
import java.util.List;

/**
 * 用于doc文档返回分页数据的描述
 * @author WX
 * @date 2020-06-08 17:07:10
 */
public class PageInfoVo<T> implements Serializable {
    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 当前页的数量
     * @ignore
     */
    @JsonIgnore
    private int size;

    //由于startRow和endRow不常用，这里说个具体的用法
    //可以在页面中"显示startRow到endRow 共size条数据"

    /**
     * 当前页面第一个元素在数据库中的行号
     * @ignore
     */
    @JsonIgnore
    private int startRow;

    /**
     * 当前页面最后一个元素在数据库中的行号
     * @ignore
     */
    @JsonIgnore
    private int endRow;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 前一页
     * @ignore
     */
    @JsonIgnore
    private int prePage;

    /**
     * 下一页
     * @ignore
     */
    @JsonIgnore
    private int nextPage;

    /**
     * 是否为第一页
     * @ignore
     */
    @JsonIgnore
    private boolean isFirstPage = false;

    /**
     * 是否为最后一页
     * @ignore
     */
    @JsonIgnore
    private boolean isLastPage = false;

    /**
     * 是否有前一页
     * @ignore
     */
    @JsonIgnore
    private boolean hasPreviousPage = false;

    /**
     * 是否有下一页
     * @ignore
     */
    @JsonIgnore
    private boolean hasNextPage = false;

    /**
     * 导航页码数
     * @ignore
     */
    @JsonIgnore
    private int navigatePages;

    /**
     * 所有导航页号
     * @ignore
     */
    @JsonIgnore
    private int[] navigatepageNums;

    /**
     * 导航条上的第一页
     */
    private int navigateFirstPage;

    /**
     * 导航条上的最后一页
     */
    private int navigateLastPage;

    /**
     * 总记录数
     */
    protected long    total;

    /**
     * 结果集
     */
    protected List<T> list;


    public static <T> PageInfoVo<T> getInstance(){
        return new PageInfoVo<T>();
    }

    public int getPageNum() {
        return pageNum;
    }

    public PageInfoVo<T> setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageInfoVo<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PageInfoVo<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public int getStartRow() {
        return startRow;
    }

    public PageInfoVo<T> setStartRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public int getEndRow() {
        return endRow;
    }

    public PageInfoVo<T> setEndRow(int endRow) {
        this.endRow = endRow;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public PageInfoVo<T> setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public int getPrePage() {
        return prePage;
    }

    public PageInfoVo<T> setPrePage(int prePage) {
        this.prePage = prePage;
        return this;
    }

    public int getNextPage() {
        return nextPage;
    }

    public PageInfoVo<T> setNextPage(int nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public PageInfoVo<T> setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public PageInfoVo<T> setLastPage(boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public PageInfoVo<T> setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public PageInfoVo<T> setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public PageInfoVo<T> setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
        return this;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public PageInfoVo<T> setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
        return this;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public PageInfoVo<T> setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
        return this;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public PageInfoVo<T> setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageInfoVo<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<T> getList() {
        return list;
    }

    public PageInfoVo<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

}
