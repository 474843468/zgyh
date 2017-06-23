package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery;

/**
 * Created by yangle on 2016/12/15.
 *  描述: 查询卡支持的卡品牌params
 */
public class PsnCardBrandQueryParams {
    // 卡号
    private String cardNo;
    // 账户标识
    private String accountId;

    private String conversationId;

    public PsnCardBrandQueryParams(String cardNo, String accountId,String conversationId) {
        this.cardNo = cardNo;
        this.accountId = accountId;
        this.conversationId = conversationId;
    }

    public PsnCardBrandQueryParams() {
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
