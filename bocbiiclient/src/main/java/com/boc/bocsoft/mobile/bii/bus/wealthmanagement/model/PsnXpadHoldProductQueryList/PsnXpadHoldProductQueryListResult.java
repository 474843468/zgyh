package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList;

import java.io.Serializable;

/**
 * I42-4.12 012持有产品查询与赎回  PsnXpadHoldProductQueryList  响应Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadHoldProductQueryListResult implements Serializable{
    //产品销售状态
    private String status;
    //钞汇标识
    private String cashRemit;
    private String remark;
    //购买价格
    private int buyPrice;
    //产品币种
    private String curCode;
    //产品代码
    private String prodCode;
    //产品名称
    private String prodName;
    //产品起息日
    private String prodBegin;
    //产品到期日
    private String prodEnd;
    //产品风险级别
    private String prodRisklvl;
    //产品名牌编码
    private String brandId;
    //赎回价格
    private int sellPrice;
    //产品名牌
    private String brandName;
    //预计年收益率（%）
    private String yearlyRR;
    //产品期限
    private String prodTimeLimit;
    //适用对象
    private String applyObj;
    //产品种类
    private String productCat;
    //认购起点金额
    private String buyStartingAmount;
    //追加认申购起点金额
    private String appendStartingAmount;
    //销售起始日期
    private String sellingStartingDate;
    //销售结束日期
    private String sellingEndingDate;
    //是否周期性产品
    private boolean periodical;
    private String remainCycleCount;
    //持有份额
    private int holdingQuantity;
    //可用份额
    private int availableQuantity;
    //是否可赎回
    private boolean canRedeem;
    //是否允许部分赎回
    private boolean canPartlyRedeem;
    //是否可修改分红方式
    private boolean canChangeBonusMode;
    //当前分红方式
    private String currentBonusMode;
    //最低持有份额
    private int lowestHoldQuantity;
    //赎回起点金额
    private int redeemStartingAmount;
    //已付收益
    private int payProfit;
    //是否收益累计产品
    private int progressionflag;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdBegin() {
        return prodBegin;
    }

    public void setProdBegin(String prodBegin) {
        this.prodBegin = prodBegin;
    }

    public String getProdEnd() {
        return prodEnd;
    }

    public void setProdEnd(String prodEnd) {
        this.prodEnd = prodEnd;
    }

    public String getProdRisklvl() {
        return prodRisklvl;
    }

    public void setProdRisklvl(String prodRisklvl) {
        this.prodRisklvl = prodRisklvl;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getYearlyRR() {
        return yearlyRR;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public String getApplyObj() {
        return applyObj;
    }

    public void setApplyObj(String applyObj) {
        this.applyObj = applyObj;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public String getBuyStartingAmount() {
        return buyStartingAmount;
    }

    public void setBuyStartingAmount(String buyStartingAmount) {
        this.buyStartingAmount = buyStartingAmount;
    }

    public String getAppendStartingAmount() {
        return appendStartingAmount;
    }

    public void setAppendStartingAmount(String appendStartingAmount) {
        this.appendStartingAmount = appendStartingAmount;
    }

    public String getSellingStartingDate() {
        return sellingStartingDate;
    }

    public void setSellingStartingDate(String sellingStartingDate) {
        this.sellingStartingDate = sellingStartingDate;
    }

    public String getSellingEndingDate() {
        return sellingEndingDate;
    }

    public void setSellingEndingDate(String sellingEndingDate) {
        this.sellingEndingDate = sellingEndingDate;
    }

    public boolean isPeriodical() {
        return periodical;
    }

    public void setPeriodical(boolean periodical) {
        this.periodical = periodical;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
    }

    public int getHoldingQuantity() {
        return holdingQuantity;
    }

    public void setHoldingQuantity(int holdingQuantity) {
        this.holdingQuantity = holdingQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public boolean isCanRedeem() {
        return canRedeem;
    }

    public void setCanRedeem(boolean canRedeem) {
        this.canRedeem = canRedeem;
    }

    public boolean isCanPartlyRedeem() {
        return canPartlyRedeem;
    }

    public void setCanPartlyRedeem(boolean canPartlyRedeem) {
        this.canPartlyRedeem = canPartlyRedeem;
    }

    public boolean isCanChangeBonusMode() {
        return canChangeBonusMode;
    }

    public void setCanChangeBonusMode(boolean canChangeBonusMode) {
        this.canChangeBonusMode = canChangeBonusMode;
    }

    public String getCurrentBonusMode() {
        return currentBonusMode;
    }

    public void setCurrentBonusMode(String currentBonusMode) {
        this.currentBonusMode = currentBonusMode;
    }

    public int getLowestHoldQuantity() {
        return lowestHoldQuantity;
    }

    public void setLowestHoldQuantity(int lowestHoldQuantity) {
        this.lowestHoldQuantity = lowestHoldQuantity;
    }

    public int getRedeemStartingAmount() {
        return redeemStartingAmount;
    }

    public void setRedeemStartingAmount(int redeemStartingAmount) {
        this.redeemStartingAmount = redeemStartingAmount;
    }

    public int getPayProfit() {
        return payProfit;
    }

    public void setPayProfit(int payProfit) {
        this.payProfit = payProfit;
    }

    public int getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(int progressionflag) {
        this.progressionflag = progressionflag;
    }
}
