package com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation;

import com.chinamworld.bocmbci.model.httpmodel.sifang.BaseSFForexRequestModel;

/**外汇、贵金属单笔行情查询
 *查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘单个货币对行情、详情基本信息。
 * Created by wuhan on 2016/10/27.
 */
public class QuerySingelQuotationRequestParams extends BaseSFForexRequestModel {

    private String ccygrp;
    private String cardType;
    private String cardClass;

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


   public QuerySingelQuotationRequestParams(String ccygrp,String cardType,String cardClass){
        this.ccygrp = ccygrp;
        this.cardType = cardType;
        this.cardClass = cardClass;
   }

    @Override
    public Object getExtendParam() {
        return "querySingelQuotation";
    }
}
