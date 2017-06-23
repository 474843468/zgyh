package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd;

/**
 * Created by wangtong on 2016/8/16.
 */
public class PsnSsmMessageAddParam {

    private String conversationId;
    private String accountId;
    private String ssmserviceid;
    private String pushchannel;
    private String pushterm;
    private String mobileConfirmCode;
    private String feestandard;
    private String feeway;
    private String feetype;
    private String loweramount;
    private String upperamount;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSsmserviceid() {
        return ssmserviceid;
    }

    public void setSsmserviceid(String ssmserviceid) {
        this.ssmserviceid = ssmserviceid;
    }

    public String getPushchannel() {
        return pushchannel;
    }

    public void setPushchannel(String pushchannel) {
        this.pushchannel = pushchannel;
    }

    public String getPushterm() {
        return pushterm;
    }

    public void setPushterm(String pushterm) {
        this.pushterm = pushterm;
    }

    public String getMobileConfirmCode() {
        return mobileConfirmCode;
    }

    public void setMobileConfirmCode(String mobileConfirmCode) {
        this.mobileConfirmCode = mobileConfirmCode;
    }

    public String getFeestandard() {
        return feestandard;
    }

    public void setFeestandard(String feestandard) {
        this.feestandard = feestandard;
    }

    public String getFeeway() {
        return feeway;
    }

    public void setFeeway(String feeway) {
        this.feeway = feeway;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public String getLoweramount() {
        return loweramount;
    }

    public void setLoweramount(String loweramount) {
        this.loweramount = loweramount;
    }

    public String getUpperamount() {
        return upperamount;
    }

    public void setUpperamount(String upperamount) {
        this.upperamount = upperamount;
    }
}
