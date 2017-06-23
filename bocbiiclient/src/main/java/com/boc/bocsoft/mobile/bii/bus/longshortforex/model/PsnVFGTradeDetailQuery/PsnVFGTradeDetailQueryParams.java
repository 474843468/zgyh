package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * 双向宝交易查询详情   026  PsnVFGTradeDetailQuery保证金单笔交易明细查询
 */
public class PsnVFGTradeDetailQueryParams {
    /**
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * vfgTransactionId : 123
     * internalSeq : 2
     */
    //会话
    private String conversationId;
    //交易序号,就是012接口中consignNumber，挂单/交易序号
    private String vfgTransactionId;
    //内部序号
    private String internalSeq;

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setVfgTransactionId(String vfgTransactionId) {
        this.vfgTransactionId = vfgTransactionId;
    }

    public void setInternalSeq(String internalSeq) {
        this.internalSeq = internalSeq;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getVfgTransactionId() {
        return vfgTransactionId;
    }

    public String getInternalSeq() {
        return internalSeq;
    }
}
