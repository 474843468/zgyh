package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry;

/**
 * Created by XieDu on 2016/7/26.
 */
public class PsnOnLineLoanBranchQryParams {

    private String cityCode;
    private String productCode;
    /**
     * 页面大小
     */
    private String pageSize;
    /**
     * 当前页索引
     */
    private String currentIndex;
    /**
     * 刷新标识
     * true：重新查询结果(在交易改变查询条件时是需要重新查询的,currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果
     */
    private String _refresh;
    private String conversationId;

    public String getCityCode() { return cityCode;}

    public void setCityCode(String cityCode) { this.cityCode = cityCode;}

    public String getProductCode() { return productCode;}

    public void setProductCode(String productCode) { this.productCode = productCode;}

    public String getPageSize() { return pageSize;}

    public void setPageSize(String pageSize) { this.pageSize = pageSize;}

    public String getCurrentIndex() { return currentIndex;}

    public void setCurrentIndex(String currentIndex) { this.currentIndex = currentIndex;}

    public String get_refresh() { return _refresh;}

    public void set_refresh(String _refresh) { this._refresh = _refresh;}

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}
}
