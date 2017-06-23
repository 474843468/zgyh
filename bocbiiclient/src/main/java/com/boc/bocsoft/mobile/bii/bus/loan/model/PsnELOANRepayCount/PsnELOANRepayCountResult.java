package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount;

import java.util.List;

/**
 * Created by liuzc on 2016/9/19.
 *中银E贷提前还款手续费试算返回参数
 */
public class PsnELOANRepayCountResult {
    private String loanActNum; //贷款账号
    private String advanceRepayCapital; //提前还款本金
    private String advanceRepayInterest; //提前还款利息
    private String charges; //手续费

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
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

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
