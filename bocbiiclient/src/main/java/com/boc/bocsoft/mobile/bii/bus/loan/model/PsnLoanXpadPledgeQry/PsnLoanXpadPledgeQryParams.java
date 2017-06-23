package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry;

/**
 * PsnLoanXpadPledgeQry理财质押贷款申请结果查询
 * 20s后调用此接口查询贷款申请结果
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeQryParams {
    /**
     * 网银交易流水号
     */
    private String transId;
    /**
     * 贷款品种
     * 固定值为“FIN-LOAN”
     */
    private String loanType;
    /**
     * 是否最后一次调用此接口
     * Y：是 ; N：否
     */
    private String isLastFlag;

    private String conversationId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getIsLastFlag() {
        return isLastFlag;
    }

    public void setIsLastFlag(String isLastFlag) {
        this.isLastFlag = isLastFlag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
