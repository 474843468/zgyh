package com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation;


import com.chinamworld.bocmbci.model.httpmodel.sifang.BaseSFForexRequestModel;

/**
 * 外汇、贵金属多笔行情查询
 * 查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息
 * Created by yuht on 2016/10/19.
 */
public class QueryMultipleQuotationRequestParams extends BaseSFForexRequestModel {
    private String cardType;
    private String cardClass;
    private String pSort;

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


    public String getpSort() {
        return pSort;
    }

    public void setpSort(String pSort) {
        this.pSort = pSort;
    }

    public QueryMultipleQuotationRequestParams(String cardType, String cardClass, String pSort){
        this.cardType = cardType;
        this.cardClass = cardClass;
        this.pSort = pSort;
    }
    @Override
    public Object getExtendParam() {
        return "queryMultipleQuotation";
    }


}
