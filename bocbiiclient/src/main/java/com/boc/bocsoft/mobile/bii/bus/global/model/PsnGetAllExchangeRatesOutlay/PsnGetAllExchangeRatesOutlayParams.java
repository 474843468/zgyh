package com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay;

/**
 * 登录前贵金属、外汇、双向宝行情查询-上送报文
 * Created by lxw on 2016/8/22 0022.
 */
public class PsnGetAllExchangeRatesOutlayParams {


    /**
     * ibknum : 40142
     * offerType : R
     * paritiesType : G
     */

    private String ibknum;
    private String offerType;
    private String paritiesType;

    public String getIbknum() {
        return ibknum;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getParitiesType() {
        return paritiesType;
    }

    public void setParitiesType(String paritiesType) {
        this.paritiesType = paritiesType;
    }
}
