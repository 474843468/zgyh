package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery;

/**
 * Created by huixiaobo on 2016/6/24.
 * 中银E贷用款详情上送参数
 */
public class PsnDrawingDetailParams {
    /**贷款账号*/
    private String loanAccount;

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    @Override
    public String toString() {
        return "PsnDrawingDetailParams{" +
                "loanAccount='" + loanAccount + '\'' +
                '}';
    }
}
