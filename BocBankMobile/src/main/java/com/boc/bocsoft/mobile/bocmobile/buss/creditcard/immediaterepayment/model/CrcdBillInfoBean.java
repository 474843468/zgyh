package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/24 18:47
 *
 */
public class CrcdBillInfoBean {
    /**
     *  币种 001=人民币元
     014=美元
     027=日元
     038=欧元
     012=英镑
     013=港币
     028=加拿大元
     029=澳大利亚元
     038=欧元
     */
    private String currency;

    private String crcdAccNo;
    private String crcdAccId;

    /**
     *   本期账单金额
     */
    private BigDecimal billAmount;
    /**
     * 本期应还最小金额
     */
    private BigDecimal billLimitAmout;
    /**
     * 本期未还金额
     */
    private BigDecimal haveNotRepayAmout;


    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getBillLimitAmout() {
        return billLimitAmout;
    }

    public void setBillLimitAmout(BigDecimal billLimitAmout) {
        this.billLimitAmout = billLimitAmout;
    }

    public String getCrcdAccNo() {
        return crcdAccNo;
    }

    public void setCrcdAccNo(String crcdAccNo) {
        this.crcdAccNo = crcdAccNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getHaveNotRepayAmout() {
        return haveNotRepayAmout;
    }

    public void setHaveNotRepayAmout(BigDecimal haveNotRepayAmout) {
        this.haveNotRepayAmout = haveNotRepayAmout;
    }

    public String getCrcdAccId() {
        return crcdAccId;
    }

    public void setCrcdAccId(String crcdAccId) {
        this.crcdAccId = crcdAccId;
    }
}
