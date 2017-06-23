package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup;

/**
 * Name: liukai
 * Time：2016/11/22 16:49.
 * Created by lk7066 on 2016/11/22.
 * It's used to 信用卡还款方式设定（I27/003）
 */

public class PsnCrcdPaymentWaySetupParams {


    /**
     * accountId : 208367999
     * repayType : 1
     * autoRepayMode : FULL
     * repayCurSel : 0
     * rmbRepayAcctId :
     * foreignRepayAcctId :
     * repayAcctId : 208367002
     * signForeignCurrencyAcctId :
     * token : cecvnuw6
     * conversationId : 6d48fe12-e34a-4089-926d-294ff6ee76ac
     */

    private String accountId;
    private String repayType;
    private String autoRepayMode;
    private String repayCurSel;
    private String rmbRepayAcctId;
    private String foreignRepayAcctId;
    private String repayAcctId;
    private String signForeignCurrencyAcctId;
    private String token;
    private String conversationId;
    private String devicePrint;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getAutoRepayMode() {
        return autoRepayMode;
    }

    public void setAutoRepayMode(String autoRepayMode) {
        this.autoRepayMode = autoRepayMode;
    }

    public String getRepayCurSel() {
        return repayCurSel;
    }

    public void setRepayCurSel(String repayCurSel) {
        this.repayCurSel = repayCurSel;
    }

    public String getRmbRepayAcctId() {
        return rmbRepayAcctId;
    }

    public void setRmbRepayAcctId(String rmbRepayAcctId) {
        this.rmbRepayAcctId = rmbRepayAcctId;
    }

    public String getForeignRepayAcctId() {
        return foreignRepayAcctId;
    }

    public void setForeignRepayAcctId(String foreignRepayAcctId) {
        this.foreignRepayAcctId = foreignRepayAcctId;
    }

    public String getRepayAcctId() {
        return repayAcctId;
    }

    public void setRepayAcctId(String repayAcctId) {
        this.repayAcctId = repayAcctId;
    }

    public String getSignForeignCurrencyAcctId() {
        return signForeignCurrencyAcctId;
    }

    public void setSignForeignCurrencyAcctId(String signForeignCurrencyAcctId) {
        this.signForeignCurrencyAcctId = signForeignCurrencyAcctId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    @Override
    public String toString() {
        return "PsnCrcdPaymentWaySetupParams{" +
                "accountId='" + accountId + '\'' +
                ", repayType='" + repayType + '\'' +
                ", autoRepayMode='" + autoRepayMode + '\'' +
                ", repayCurSel='" + repayCurSel + '\'' +
                ", rmbRepayAcctId='" + rmbRepayAcctId + '\'' +
                ", foreignRepayAcctId='" + foreignRepayAcctId + '\'' +
                ", repayAcctId='" + repayAcctId + '\'' +
                ", signForeignCurrencyAcctId='" + signForeignCurrencyAcctId + '\'' +
                ", token='" + token + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", devicePrint='" + devicePrint + '\'' +
                '}';
    }
}
