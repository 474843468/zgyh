package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class PsnCardBrandQueryResult {

   private String cardBrandId;//卡品牌编号

    public PsnCardBrandQueryResult(String cardBrandId) {
        this.cardBrandId = cardBrandId;
    }

    public PsnCardBrandQueryResult() {
    }

    public String getCardBrandId() {
        return cardBrandId;
    }

    public void setCardBrandId(String cardBrandId) {
        this.cardBrandId = cardBrandId;
    }

}
