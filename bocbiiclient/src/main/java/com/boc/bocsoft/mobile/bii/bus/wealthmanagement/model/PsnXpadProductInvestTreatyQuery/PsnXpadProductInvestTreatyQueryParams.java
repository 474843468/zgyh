package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery;

/**
 * 产品投资协议查询--请求
 * Created by wangtong on 2016/10/24.
 */
public class PsnXpadProductInvestTreatyQueryParams {
    private String conversationId;// 会话ID
    private String accountId;// 账户ID
    private String proId;// 产品代码
    private String agrType;// 协议类型 0：全部1：智能投资2：定时定额投资3：周期滚续投资4：余额理财投资
    private String instType;// 投资方式
    private String pageSize;// 页面大小
    private String currentIndex;// 当前页索引
    private String _refresh;// 是否重新查询

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
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
