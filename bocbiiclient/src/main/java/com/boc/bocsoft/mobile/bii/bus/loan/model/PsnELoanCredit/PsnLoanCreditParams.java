package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit;

/**
 * Created by huixiabo on 2016/6/16.
 * 查询授信额度上送参数
 */
public class PsnLoanCreditParams {
    /**贷款产品编号 固定值OC-LOAN*/
    private String loanPrdNo;

    public String getLoanPrdNo() {
        return loanPrdNo;
    }

    public void setLoanPrdNo(String loanPrdNo) {
        this.loanPrdNo = loanPrdNo;
    }

    @Override
    public String toString() {
        return "PsnLoanCreditParams{" +
                "loanPrdNo='" + loanPrdNo + '\'' +
                '}';
    }
}
