package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 15:02
 * 描述：支付解约预交易
 */
public class PsnLoanPaymentUnsignVerifyParams {
    private String conversationId;

    /**
     * 额度编号/贷款账号
     * 当“额度/账户签约标识”上送为F时，此字段上送额度编号；
     * 当“额度/账户签约标识”上送为A时，此字段上送贷款账号
     */
    private String quoteOrActNo;
    /**
     * 取值范围：
     * 额度为F
     * 账户为A
     */
    private String quoteFlag;
    /**
     * 签约账户
     */
    private String signAccountNum;
    private String _combinId;

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

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
