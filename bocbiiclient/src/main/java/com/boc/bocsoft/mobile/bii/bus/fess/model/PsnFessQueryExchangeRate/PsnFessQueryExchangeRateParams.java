package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate;

/**I49 4.6 006PsnFessQueryExchangeRate查询结购汇当前参考牌价
 * 701批次废弃 使用新接口024PsnFessQueryExchangeRebateRate查询结购汇优惠后牌价信息
 *
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQueryExchangeRateParams {
    private String accountId;//	账户ID	String 	M
    private String currency;//	币种	String	M
    private String fessFlag;//	结售汇标识	String	M	S=结汇 B=购汇
    private String cashRemit;//	钞汇标识	String		结汇必输

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFessFlag() {
        return fessFlag;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }
}
