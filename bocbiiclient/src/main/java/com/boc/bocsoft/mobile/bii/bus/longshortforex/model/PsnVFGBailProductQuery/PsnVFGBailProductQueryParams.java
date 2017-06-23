package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery;

/**
 * Params：查询可签约保证金产品
 * Created by zhx on 2016/11/21
 */
public class PsnVFGBailProductQueryParams {

    /**
     * currentIndex : 0
     * pageSize : 10
     */
    // 起始序号
    private String currentIndex;
    // 每页数
    private String pageSize;
    // 刷新标志(true：刷新 false：不刷新 非必输)
    private String _refresh;
    private String conversationId;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
