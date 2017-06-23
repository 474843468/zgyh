package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit;

/**
 * Created by Administrator on 2016/6/16.
 * 查询授信额度返回参数
 */
public class PsnLoanCreditResult {

    /**额度*/
    private String quote;
    /**额度编号*/
    private String quoteNo;
    /**额度状态  01:未激活 02:审批中 03:已激活 04:已拒绝 05:不存在*/
    private String quoteState;


    public String getQuote() {
        return quote;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public String getQuoteState() {
        return quoteState;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public void setQuoteState(String quoteState) {
        this.quoteState = quoteState;
    }

    @Override
    public String toString() {
        return "PsnLoanCreditResult{" +
                "quote='" + quote + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", quoteState='" + quoteState + '\'' +
                '}';
    }
}
