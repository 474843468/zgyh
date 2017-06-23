package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans;

/**
 * 手机取款 -- 汇出查询 -- 撤销交易
 * Created by wangf on 2016/7/13.
 */
public class PsnMobileCancelTransParams {


    /**
     * remitNo : 000024373
     * token : ar28277y
     */

    //汇款编号
    private String remitNo;
    private String token;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
