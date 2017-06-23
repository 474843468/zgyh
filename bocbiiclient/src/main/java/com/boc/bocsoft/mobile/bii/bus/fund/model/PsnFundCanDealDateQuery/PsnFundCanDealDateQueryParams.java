package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery;

/**
 * Created by zcy7065 on 2016/11/23.
 */
public class PsnFundCanDealDateQueryParams {

    /**
     * fundCode : 基金代码
     * appointFlag : 指定日期交易标识 0：购买 1：赎回
     */
    private String fundCode;
    private String appointFlag;

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setAppointFlag(String appointFlag) {
        this.appointFlag = appointFlag;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getAppointFlag() {
        return appointFlag;
    }
}
