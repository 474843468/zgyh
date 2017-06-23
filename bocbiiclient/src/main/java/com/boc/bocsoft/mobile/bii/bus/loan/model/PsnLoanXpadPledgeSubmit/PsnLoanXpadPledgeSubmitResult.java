package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit;

/**
 * 是否需要申请Conversation	True
 * 调用此接口上送的conversationId需与PsnLoanXpadPledgeProductQry接口保持一致
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeSubmitResult {
    /**
     * 网银交易流水号
     */
    private String transId;

    /**
     * 贷款品种
     * 固定值为“FIN-LOAN
     */
    private String loanType;

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
}
