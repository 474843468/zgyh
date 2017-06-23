package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery;

/**
 * 客户投资智能协议查询
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadCapacityQueryParams {
    /**
     * 协议类型
     * 0：全部
     * 1：智能投资
     * 2：定时定额投资
     * 3：周期滚续投资
     * 4：余额理财投资
     */
    private String agrType;
    /**
     * 协议状态
     * 0：全部
     * 1：正常
     * 2：失效
     */
    private String agrState;
    /**
     * 页面大小
     */
    private String pageSize;
    /**
     * 当前页索引
     */
    private String currentIndex;
    /**
     * 是否重新查询
     * true：重新查询结果(在交易改变查询条件时是需要重新查询的,
     * currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果
     */
    private String _refresh;

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    /**
     * 会话Id
     */
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getAgrState() {
        return agrState;
    }

    public void setAgrState(String agrState) {
        this.agrState = agrState;
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


}
