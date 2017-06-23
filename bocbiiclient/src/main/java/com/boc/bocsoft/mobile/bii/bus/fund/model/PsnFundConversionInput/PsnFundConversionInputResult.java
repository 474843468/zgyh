package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput;

/**
 * Created by pactera on 2016/12/13.
 */

public class PsnFundConversionInputResult {

    /**
     * fundCode : 220020
     * fundName : 华夏超短
     * canBuy : true
     * tranState :
     */

    private String fundCode;
    private String fundName;
    private String canBuy;
    private String tranState;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(String canBuy) {
        this.canBuy = canBuy;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }
}
