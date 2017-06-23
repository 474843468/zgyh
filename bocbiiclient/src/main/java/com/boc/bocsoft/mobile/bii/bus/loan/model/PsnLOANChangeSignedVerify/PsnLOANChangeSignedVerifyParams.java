package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 15:18
 * 描述：支付签约修改预交易
 */
public class PsnLOANChangeSignedVerifyParams {
    private String conversationId;
    private String quoteOrActNo;
    private String quoteFlag;
    private String usePref;
    private String _combinId;
    /**
     * 签约账户Id
     */
    private String signAccountId;
    /**
     * 签约账户
     */
    private String signAccountNum;
    private String oldsignAccount;
    private String signPeriod;
    private String repayFlag;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuoteOrActNo() {
        return quoteOrActNo;
    }

    public void setQuoteOrActNo(String quoteOrActNo) {
        this.quoteOrActNo = quoteOrActNo;
    }

    public String getQuoteFlag() {
        return quoteFlag;
    }

    public void setQuoteFlag(String quoteFlag) {
        this.quoteFlag = quoteFlag;
    }

    public String getUsePref() {
        return usePref;
    }

    public void setUsePref(String usePref) {
        this.usePref = usePref;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getSignAccountId() {
        return signAccountId;
    }

    public void setSignAccountId(String signAccountId) {
        this.signAccountId = signAccountId;
    }

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public String getOldsignAccount() {
        return oldsignAccount;
    }

    public void setOldsignAccount(String oldsignAccount) {
        this.oldsignAccount = oldsignAccount;
    }

    public String getSignPeriod() {
        return signPeriod;
    }

    public void setSignPeriod(String signPeriod) {
        this.signPeriod = signPeriod;
    }

    public String getRepayFlag() {
        return repayFlag;
    }

    public void setRepayFlag(String repayFlag) {
        this.repayFlag = repayFlag;
    }
}
