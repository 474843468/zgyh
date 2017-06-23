package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit;

/**
 * Created by XieDu on 2016/7/27.
 */
public class PsnOnLineLoanSubmitResult {

    /**
     * 申请结果
     * 1:提交成功
     * 2:提交失败
     */
    private String applyResult;
    /**
     * 贷款编号 16位数字
     */
    private String loanNumber;

    public String getApplyResult() { return applyResult;}

    public void setApplyResult(String applyResult) { this.applyResult = applyResult;}

    public String getLoanNumber() { return loanNumber;}

    public void setLoanNumber(String loanNumber) { this.loanNumber = loanNumber;}
}
