package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery;

/**
 * 其他类型贷款-还款计划-剩余还款计划请求参数
 * Created by liuzc on 2016/8/11.
 */
public class PsnLOANRemainQueryParams {

    /**
     * actNum : 00000102811811890
     * pageSize : 10
     * currentIndex : 0
     * _refresh : false
     * conversationId : 15d1fb69-45f1-446c-b9e2-f85bb71cee74
     */

    private String actNum; //贷款账号
    private String pageSize; //每页显示条数
    private String currentIndex; //当前页
    private String _refresh; //刷新标志
    private String conversationId; //会话ID

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
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
