package com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getallexchangeratesoutlay;

/**
 * Created by gwluo on 2016/9/3.
 */
public class GetAllExchangeRatesOutlayParams {

    private String ibknum;//省行联行号	String	M	客户登记的交易账户的省行联行号
    private String offerType;//实虚盘标识	String	M	实盘：R  虚盘（双向宝）：M
    private String paritiesType;//汇率类型	String	M	外汇：F   黄金：G

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
