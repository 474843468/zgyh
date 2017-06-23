package com.chinamworld.bocmbci.widget.adapter;

/**
 * Created by Administrator on 2016/11/10.
 */
public class InvestListItem {
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public int getUpAndDownFlag() {
        return upAndDownFlag;
    }

    public void setUpAndDownFlag(int upAndDownFlag) {
        this.upAndDownFlag = upAndDownFlag;
    }

    public String getOffPercentage() {
        return offPercentage;
    }

    public void setOffPercentage(String offPercentage) {
        this.offPercentage = offPercentage;
    }

    public String getriseOrFall() {
        return riseOrFall;
    }

    public void setriseOrFall(String off) {
        this.riseOrFall = off;
    }

    /** 币种 */
    private String codeName;
    /** 买入价 */
    private String buy;
    /** 卖出价 */
    private String sell;
    /** 涨跌值 */
    private String riseOrFall;
    /** 涨跌幅 */
    private String offPercentage;
    /** 1:平，2，涨，3，跌  */
    private int upAndDownFlag;
}
