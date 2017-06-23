package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms;

/**
 * 手机取款 -- 汇出查询 -- 重新发送短信
 * Created by wangf on 2016/7/13.
 */
public class PsnMobileResetSendSmsParams {


    /**
     * fromName : zhangsan
     * payeeMobile : 13452336254
     * remitAmount : 100
     * remitCurrencyCode : 001
     * remitNo : 567890112
     * remitStatus : OU
     */

    //汇款人户名
    private String fromName;
    //收款人手机号
    private String payeeMobile;
    //汇款金额
    private String remitAmount;
    //汇款币种
    private String remitCurrencyCode;
    //汇款编号
    private String remitNo;
    //汇款状态
    private String remitStatus;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(String remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getRemitCurrencyCode() {
        return remitCurrencyCode;
    }

    public void setRemitCurrencyCode(String remitCurrencyCode) {
        this.remitCurrencyCode = remitCurrencyCode;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getRemitStatus() {
        return remitStatus;
    }

    public void setRemitStatus(String remitStatus) {
        this.remitStatus = remitStatus;
    }
}
