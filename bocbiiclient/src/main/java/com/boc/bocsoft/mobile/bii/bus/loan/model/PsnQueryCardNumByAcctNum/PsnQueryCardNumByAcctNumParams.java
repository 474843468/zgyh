package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryCardNumByAcctNum;

/**
 * Created by louis.hui on 2016/10/17.
 */
public class PsnQueryCardNumByAcctNumParams {
    /**账号*/
    private String acctNum;

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    @Override
    public String toString() {
        return "PsnQueryCardNumByAcctNumParams{" +
                "acctNum='" + acctNum + '\'' +
                '}';
    }
}
