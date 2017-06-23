package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete;

/**
 * 预约交易删除
 * Created by wangf on 2016/7/21.
 */
public class PsnTransPreRecordDeleteParams {

    /**
     * batSeq : 2673813661
     * dateType : 0
     * token : mpde2y6u
     * transactionId : 3867932929
     */

    //转账批次号
    private String batSeq;
    //日期查询类型
    private String dateType;
    //网银交易序号
    private String transactionId;

    //防重机制
    private String token;
    //会话ID
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(String batSeq) {
        this.batSeq = batSeq;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
