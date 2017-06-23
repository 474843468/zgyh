package com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre;

/**
 * Created by wangtong on 2016/6/27.
 */
public class PsnSsmMessageChangePreParam {

    private String conversationId;
    private String accountId;
    private String accountNumber;
    private String _combinId;
    private String telnumnew;
    private String pushterm;
    private String mobileConfirmCode;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getTelnumnew() {
        return telnumnew;
    }

    public void setTelnumnew(String telnumnew) {
        this.telnumnew = telnumnew;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
