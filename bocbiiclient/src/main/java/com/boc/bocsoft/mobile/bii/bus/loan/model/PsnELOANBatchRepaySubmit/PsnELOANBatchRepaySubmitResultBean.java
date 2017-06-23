package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit;

import java.util.List;

/**
 * 中银E贷-批量提前还款返回 list item
 * Created by lzc4524 on 2016/8/31.
 */
public class PsnELOANBatchRepaySubmitResultBean {
    private String loanAccount; //贷款账号
    private String transactionId; //网银交易流水号
    private String payAccount; //还款账户
    private String advanceRepayCapital; //提前还款本金
    private String advanceRepayInterest; //提前还款利息
    private String status; //交易状态,A:交易成功，B:交易失败
    private String errorCode; //错误码
    private String errorMsg; //错误信息

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getAdvanceRepayCapital() {
        return advanceRepayCapital;
    }

    public void setAdvanceRepayCapital(String advanceRepayCapital) {
        this.advanceRepayCapital = advanceRepayCapital;
    }

    public String getAdvanceRepayInterest() {
        return advanceRepayInterest;
    }

    public void setAdvanceRepayInterest(String advanceRepayInterest) {
        this.advanceRepayInterest = advanceRepayInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
