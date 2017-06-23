package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/21 18:52
 * 信用卡购汇还款提交
 */
public class PsnCrcdForeignPayOffResult {

    /**
    *   转入账户信息
    */
    private PsnCommonQueryAllChinaBankAccountResult crcdAcct;
    /**
    *   转出账户信息
    */
    private PsnCommonQueryAllChinaBankAccountResult rmbAcct;
    /**
     *  交易流水号
     */
    private String transactionId;
    /**
     *  汇率
     */
    private BigDecimal exchangeRate;
    /**
     *  还款金额（人民币元）
     */
    private BigDecimal payAmt;
    /**
     *  实收费用
     */
    private BigDecimal finalCommissionCharge;



    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PsnCommonQueryAllChinaBankAccountResult getCrcdAcct() {
        return crcdAcct;
    }

    public void setCrcdAcct(PsnCommonQueryAllChinaBankAccountResult crcdAcct) {
        this.crcdAcct = crcdAcct;
    }

    public PsnCommonQueryAllChinaBankAccountResult getRmbAcct() {
        return rmbAcct;
    }

    public void setRmbAcct(PsnCommonQueryAllChinaBankAccountResult rmbAcct) {
        this.rmbAcct = rmbAcct;
    }
}
