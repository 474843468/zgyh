package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation;

/**
 * Params：涨跌幅／值：queryMultipleQuotation-请求model{查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。}
 * Created by yx on 2016年12月15日 11:05:03
 */ 
public class WFSSQueryMultipleQuotationReqModel {
    /**
     * cardType : G
     * cardClass : M
     * pSort :
     */

    /**
     * 牌价类型 长度最大1  必输{F-外汇牌价，G-贵金属牌价}
     */
    private String cardType; //
    /**
     * 牌价类型 长度最大1  必输{R-实盘(外汇)，M-虚盘(双向宝)}
     */
    private String cardClass;
    /**
     * 涨跌幅排序   长度最大2  非必输{FUP - 升序，DN - 降序 ，为空按优先级排序}
     */
    private String pSort;

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public void setPSort(String pSort) {
        this.pSort = pSort;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardClass() {
        return cardClass;
    }

    public String getPSort() {
        return pSort;
    }
}
