package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery;

/**
 * 预约交易查询
 * Created by wangf on 2016/7/21.
 */
public class PsnTransPreRecordQueryParams {


    /**
     * _refresh : false
     * currentIndex : 0
     * dateType : 0
     * endDate : 2017/12/27
     * pageSize : 15
     * startDate : 2017/12/14
     */


    private String _refresh;
    private String currentIndex;
    private String dateType;
    private String endDate;
    private String pageSize;
    private String startDate;


    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
