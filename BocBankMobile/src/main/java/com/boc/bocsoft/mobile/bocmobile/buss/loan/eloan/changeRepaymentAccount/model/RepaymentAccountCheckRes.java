package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model;

import java.util.List;

/**
 * PsnLOANPayerAcountCheck还款账户检查，返回参数
 * Created by xintong on 2016/6/24.
 */
public class RepaymentAccountCheckRes {

    private List checkResult;

    public List getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(List checkResult) {
        this.checkResult = checkResult;
    }
}
