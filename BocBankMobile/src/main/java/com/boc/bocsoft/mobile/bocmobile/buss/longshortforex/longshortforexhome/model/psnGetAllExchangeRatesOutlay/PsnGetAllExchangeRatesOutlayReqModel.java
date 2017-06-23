package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay;

/**
 * Params：双向宝-买入卖出价数据：PsnGetAllExchangeRatesOutlay-请求model
 * {I43-4.18 018 PsnGetAllExchangeRatesOutlay登录前贵金属、外汇、双向宝行情查询}
 * Created by yx on 2016年12月15日 09:23:39
 */
public class PsnGetAllExchangeRatesOutlayReqModel {

    /**
     * ibknum : 40142
     * offerType : M
     * paritiesType : G
     */
    /**
     * 省行联行号	String	必输	客户登记的交易账户的省行联行号
     */
    private String ibknum;
    /**
     * 实虚盘标识	String	必输	{实盘：R ， 虚盘（双向宝）：M}
     */
    private String offerType;
    /**
     * 汇率类型	String	必输	{外汇：F,黄金：G}
     */
    private String paritiesType;

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public void setParitiesType(String paritiesType) {
        this.paritiesType = paritiesType;
    }

    public String getIbknum() {
        return ibknum;
    }

    public String getOfferType() {
        return offerType;
    }

    public String getParitiesType() {
        return paritiesType;
    }
}
