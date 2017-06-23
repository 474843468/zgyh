package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery;

import java.io.Serializable;

/**
 * 4.34 034累进产品收益率查询PsnXpadProgressQuery  请求model
 * Created by cff on 2016/10/19.
 */
public class PsnXpadProgressQueryReqModel implements Serializable{
    /**
     *账户标识
     */
    private String accountId;
    /**
     * 账号缓存标识
     */
    private String accountKey;
    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 当前页索引
     */
    private String currentIndex;
    /**
     * 页面大小
     */
    private String pageSize;
    /**
     * 是否重新查询
     */
    private String _refresh;
    /**
     * 会话ID
     */
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
