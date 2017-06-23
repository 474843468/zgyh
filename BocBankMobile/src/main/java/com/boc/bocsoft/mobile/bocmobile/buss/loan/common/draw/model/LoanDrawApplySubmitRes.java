package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model;

/**
 * 贷款管理-循环/非循环类贷款（非中银E贷）-用款交易返回参数
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplySubmitRes {
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
