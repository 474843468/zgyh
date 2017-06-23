package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery;

/**
 * Created by huixiaobo on 2016/11/18.
 * 058失效定期定额查询—上送参数
 */
public class PsnScheduledFundUnavailableQueryParams {
    /**当前页*/
    private String currentIndex;
    /**每页显示条数*/
    private String pageSize;
    /**刷新标志*/
    private String _refresh;
    /**会话ID*/
    private String conversationId;

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

    @Override
    public String toString() {
        return "PsnScheduledFundUnavailableQueryParams{" +
                "currentIndex='" + currentIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", _refresh='" + _refresh + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
