package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 15:44
 * 描述：单贷款账户支付签约信息查询
 */
public class PsnLOANPaymentInfoQueryParams {
    private String conversationId;
    /**
     * 贷款账户
     */
    private String loanActNum;

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
