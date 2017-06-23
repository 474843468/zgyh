package com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGetAllExchangeRatesOutlay;

/**
 * 4.18 018 PsnGetAllExchangeRatesOutlay登录前贵金属、外汇、双向宝行情查询
 * Created by Administrator on 2016/8/22 0022.
 */
public class PsnGetAllExchangeRatesOutlayParams {

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
