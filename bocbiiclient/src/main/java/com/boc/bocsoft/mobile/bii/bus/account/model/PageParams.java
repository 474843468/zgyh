package com.boc.bocsoft.mobile.bii.bus.account.model;

/**
 * @author wangyang
 *         16/7/26 16:00
 *         分页参数
 */
public class PageParams extends PublicParams{

    /** 页面大小 */
    private int pageSize;
    /** 当前页记录索引 */
    private int currentIndex ;
    /** true：重新查询结果(在交易改变查询条件时是需要重新查询的，currentIndex需上送0) false:不需要重新查询，使用缓存中的结果 */
    private String _refresh;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String is_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }
}
