package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery;

/**
 * Created by zcy7065 on 2016/12/28.
 */
public class PsnQuickSellQuotaQueryResult {

    private String dayLimit;//单人单天额度
    private String dayCompleteShare;//当日已快速赎回份额
    private String totalLimit;//产品总额度
    private String dayCompleteNum;//当日已快速赎回额度
    private String totalBalance;//当日总余额
    private String perDealNum;//单人单天笔数
    private String dayFrozenLimit;//当日冻结总余额
    private String dayQuickSellNum;//当日可快速赎回笔数
    private String perLimit;//单人单笔额度
    private String dayQuickSellLimit;//当天可快速赎回额度

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public void setDayCompleteShare(String dayCompleteShare) {
        this.dayCompleteShare = dayCompleteShare;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public void setDayCompleteNum(String dayCompleteNum) {
        this.dayCompleteNum = dayCompleteNum;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public void setPerDealNum(String perDealNum) {
        this.perDealNum = perDealNum;
    }

    public void setDayFrozenLimit(String dayFrozenLimit) {
        this.dayFrozenLimit = dayFrozenLimit;
    }

    public void setDayQuickSellNum(String dayQuickSellNum) {
        this.dayQuickSellNum = dayQuickSellNum;
    }

    public void setPerLimit(String perLimit) {
        this.perLimit = perLimit;
    }

    public void setDayQuickSellLimit(String dayQuickSellLimit) {
        this.dayQuickSellLimit = dayQuickSellLimit;
    }

    public String getDayLimit() {
        return dayLimit;
    }

    public String getDayCompleteShare() {
        return dayCompleteShare;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public String getDayCompleteNum() {
        return dayCompleteNum;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public String getPerDealNum() {
        return perDealNum;
    }

    public String getDayFrozenLimit() {
        return dayFrozenLimit;
    }

    public String getDayQuickSellNum() {
        return dayQuickSellNum;
    }

    public String getPerLimit() {
        return perLimit;
    }

    public String getDayQuickSellLimit() {
        return dayQuickSellLimit;
    }
}
