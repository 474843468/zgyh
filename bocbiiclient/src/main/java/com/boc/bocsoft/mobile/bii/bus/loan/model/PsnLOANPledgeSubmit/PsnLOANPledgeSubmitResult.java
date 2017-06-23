package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit;

import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANPledgeSubmitResult {

    /**
     * 贷款账号
     */
    private String accountNumber;
    /**
     * 网银交易流水号
     */
    private String transactionId;
    /**
     * 贷款到期日
     */
    private LocalDate loanToDate;

    public String getAccountNumber() { return accountNumber;}

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber;}

    public String getTransactionId() { return transactionId;}

    public void setTransactionId(String transactionId) { this.transactionId = transactionId;}

    public LocalDate getLoanToDate() { return loanToDate;}

    public void setLoanToDate(LocalDate loanToDate) { this.loanToDate = loanToDate;}
}
