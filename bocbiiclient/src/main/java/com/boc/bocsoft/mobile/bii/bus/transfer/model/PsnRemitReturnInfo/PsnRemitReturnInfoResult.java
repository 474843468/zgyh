package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo;

import org.threeten.bp.LocalDate;

/**
 * Created by wangf on 2016/7/13.
 */
public class PsnRemitReturnInfoResult {

    //退汇交易转入账户
    private String payerActno;
    //退汇交易转出账户
    private String payeeActno;
    //退汇金额
    private String reexchangeAmount;
    //退汇原因
    private String reexchangeInfo;
    //附言
    private String remittanceInfo;
    //退汇入账日期
    private LocalDate reexchangeDate;
    //退汇交易状态
    private String reexchangeStatus;

    public String getPayerActno() {
        return payerActno;
    }

    public void setPayerActno(String payerActno) {
        this.payerActno = payerActno;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getReexchangeAmount() {
        return reexchangeAmount;
    }

    public void setReexchangeAmount(String reexchangeAmount) {
        this.reexchangeAmount = reexchangeAmount;
    }

    public String getReexchangeInfo() {
        return reexchangeInfo;
    }

    public void setReexchangeInfo(String reexchangeInfo) {
        this.reexchangeInfo = reexchangeInfo;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public LocalDate getReexchangeDate() {
        return reexchangeDate;
    }

    public void setReexchangeDate(LocalDate reexchangeDate) {
        this.reexchangeDate = reexchangeDate;
    }

    public String getReexchangeStatus() {
        return reexchangeStatus;
    }

    public void setReexchangeStatus(String reexchangeStatus) {
        this.reexchangeStatus = reexchangeStatus;
    }
}
