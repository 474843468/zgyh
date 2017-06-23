package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/6/22.
 * 授信额度查询 首次申请页面model
 */
public class EloanCreditViewModel implements Serializable {
    /**额度*/
    private String quote;
    /**额度编号*/
    private String quoteNo;
    /**额度状态 01:未激活 02:审批中 03:已激活 04:已拒绝 05:不存在*/
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
        return "EloanCreditViewModel{" +
                "quote='" + quote + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", quoteState='" + quoteState + '\'' +
                '}';
    }
}
