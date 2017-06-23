package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit;

import java.util.List;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANPledgeSubmitParams {

    private String conversationId;
    /**
     * 贷款品种	固定值为“PLEA”
     */
    private String loanType;
    /**
     * 可用金额
     */
    private String availableAmount;
    /**
     * 本次用款金额
     */
    private String amount;
    private String currencyCode;
    /**
     * 贷款期限	单位为“月”
     */
    private String loanPeriod;
    private String loanRate;
    /**
     * 还款方式
     * B：只还利息
     * F：等额本息
     * G：等额本金
     * N：协议还款
     */
    private String payType;
    /**
     * 还款周期	固定值为“01”
     */
    private String payCycle;
    /**
     * 收款账户
     */
    private String toActNum;
    /**
     * 收款账户Id
     */
    private String toAccount;
    /**
     * 还款账户Id
     */
    private String payAccount;
    /**
     * 定一本账户Id
     */
    private String accountId;
    /**
     * 手机交易码
     */
    private String Smc;
    /**
     * 动态口令
     */
    private String Otp;

    private String Smc_RC;
    private String Otp_RC;

    private String token;

    /**
     * CA认证生成的密文
     */
    private String _signedData;
    /**
     * 设备加密信息
     */
    private String deviceInfo;
    private String deviceInfo_RC;

    private String devicePrint;
    private String state;
    private String activ;

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    private List<VolNoAndCerNoListEntity> volNoAndCerNoList;

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}

    public String getLoanType() { return loanType;}

    public void setLoanType(String loanType) { this.loanType = loanType;}

    public String getAvailableAmount() { return availableAmount;}

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAmount() { return amount;}

    public void setAmount(String amount) { this.amount = amount;}

    public String getCurrencyCode() { return currencyCode;}

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}

    public String getLoanPeriod() { return loanPeriod;}

    public void setLoanPeriod(String loanPeriod) { this.loanPeriod = loanPeriod;}

    public String getLoanRate() { return loanRate;}

    public void setLoanRate(String loanRate) { this.loanRate = loanRate;}

    public String getPayType() { return payType;}

    public void setPayType(String payType) { this.payType = payType;}

    public String getPayCycle() { return payCycle;}

    public void setPayCycle(String payCycle) { this.payCycle = payCycle;}

    public String getToActNum() { return toActNum;}

    public void setToActNum(String toActNum) { this.toActNum = toActNum;}

    public String getToAccount() { return toAccount;}

    public void setToAccount(String toAccount) { this.toAccount = toAccount;}

    public String getPayAccount() { return payAccount;}

    public void setPayAccount(String payAccount) { this.payAccount = payAccount;}

    public String getAccountId() { return accountId;}

    public void setAccountId(String accountId) { this.accountId = accountId;}

    public String getSmc() { return Smc;}

    public void setSmc(String Smc) { this.Smc = Smc;}

    public String getOtp() { return Otp;}

    public void setOtp(String Otp) { this.Otp = Otp;}

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    public List<VolNoAndCerNoListEntity> getVolNoAndCerNoList() { return volNoAndCerNoList;}

    public void setVolNoAndCerNoList(List<VolNoAndCerNoListEntity> volNoAndCerNoList) {
        this.volNoAndCerNoList = volNoAndCerNoList;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class VolNoAndCerNoListEntity {
        /**
         * 存折册号
         */
        private String volNo;
        /**
         * 存单号
         */
        private String cerNo;
        private String currencyCode;
        /**
         * 存单凭证号
         */
        private String pingNo;

        private String availableBalance;

        public String getVolNo() { return volNo;}

        public void setVolNo(String volNo) { this.volNo = volNo;}

        public String getCerNo() { return cerNo;}

        public void setCerNo(String cerNo) { this.cerNo = cerNo;}

        public String getCurrencyCode() { return currencyCode;}

        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}

        public String getPingNo() { return pingNo;}

        public void setPingNo(String pingNo) { this.pingNo = pingNo;}

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }
    }
}
