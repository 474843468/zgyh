package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay;

/**
 * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询 请求
 * Created by zn on 2016/11/23.
 */
public class PsnXpadProgressQueryOutlayParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 页面大小
     */
    private String  pageSize;
    /**
     * 当前页索引
     */
    private String currentIndex;
    /**
     * 刷新标识
     */
    private String _refresh;

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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
