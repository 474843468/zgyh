package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery;

import java.io.Serializable;

/**
 * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
 * Created by cff on 2016/9/22.
 */
public class PsnXpadReferProfitDetailQueryReqModel implements Serializable{
    /**
     * 会话id
     */
    private String conversationId;
    /**
     *账户缓存标识
     */
    private String accountKey;
    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 是否收益累
     */
    private String progressionflag;
    /**
     * 产品性质
     */
    private String kind;
    /**
     * 起始日期
     */

    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 钞汇标识
     */
    private String cashRemit;
    /**
     * 份额流水号
     */
    private String tranSeq;
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
     */
    private String _refresh;

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

    public String getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
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
