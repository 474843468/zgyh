package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/7/26 17:53
 *         虚拟银行卡未出账单合计查询
 */
public class PsnCrcdVirtualCardUnsettledbillSumResult {

    /** 币种-- 001=人民币元,014=美元,027=日元 */
    private String currency;
    /** 贷方合计 */
    private BigDecimal creditSum;
    /** 借方合计 */
    private BigDecimal debitSum;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(BigDecimal creditSum) {
        this.creditSum = creditSum;
    }

    public BigDecimal getDebitSum() {
        return debitSum;
    }

    public void setDebitSum(BigDecimal debitSum) {
        this.debitSum = debitSum;
    }
}
