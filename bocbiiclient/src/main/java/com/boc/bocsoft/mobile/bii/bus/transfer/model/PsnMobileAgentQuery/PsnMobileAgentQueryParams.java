package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery;

/**
 * 代理点查询
 *
 * Created by liuweidong on 2016/7/26.
 */
public class PsnMobileAgentQueryParams {
    /**
     * 省联行号
     */
    private String prvIbkNum;
    /**
     * 当前页号
     */
    private String currentIndex;
    /**
     * 每页显示条数
     */
    private String pageSize;

    public String getPrvIbkNum() {
        return prvIbkNum;
    }

    public void setPrvIbkNum(String prvIbkNum) {
        this.prvIbkNum = prvIbkNum;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
