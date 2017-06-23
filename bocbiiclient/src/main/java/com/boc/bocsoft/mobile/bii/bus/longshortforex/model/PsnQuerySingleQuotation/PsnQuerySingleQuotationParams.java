package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnQuerySingleQuotation;

/**
 * @author wangyang
 *         2016/12/19 11:18
 *         查询单笔外汇/贵金属行情
 */
public class PsnQuerySingleQuotationParams {

    /** 货币对代码 sourceCurrencyCode+targetCurrencyCode */
    private String ccygrp;
    /** 牌价类型 F-外汇牌价,G-贵金属牌价*/
    private String cardType;
    /** 牌价种类 R-实盘(外汇),M-虚盘(双向宝)*/
    private String cardClass = "M";

    public String getCcygrp() {
        return ccygrp;
    }

    public void setCcygrp(String ccygrp) {
        this.ccygrp = ccygrp;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardClass() {
        return cardClass;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }
}
