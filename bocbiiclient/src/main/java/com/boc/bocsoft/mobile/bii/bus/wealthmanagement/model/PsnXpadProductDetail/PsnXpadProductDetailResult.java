package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetail;

import java.math.BigDecimal;

/**
 * 产品详情查询_结果
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnXpadProductDetailResult {

    // 产品代码
    private String prodCode;
    // 产品名称
    private String prodName;
    // 产品币种
    private String curCode;
    // 预计年收益率（%）
    private String yearlyRR;
    // 购买价格
    private BigDecimal buyPrice;
    // 产品期限
    private String prodTimeLimit;
    // 适用对象
    private String applyObj;
    // 产品销售状态
    private String status;
    // 产品风险级别
    private String prodRisklvl;
    // 是否周期性产品
    private boolean periodical;
    // 产品种类
    private String productCat;
    // 认购起点金额
    private BigDecimal buyStartingAmount;
    // 追加认申购起点金额
    private BigDecimal appendStartingAmount;
    private String sellingStartingDate;
    // 销售结束日期
    private String sellingEndingDate;
    // 产品成立日
    private String prodBegin;
    // 产品到期日
    private String prodEnd;
    // 产品种类
    private String brandName;
    // 是否收益累计产品
    private String progressionflag;
    // 风险揭示信息
    private String riskMsg;

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

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getYearlyRR() {
        return yearlyRR;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProdRisklvl() {
        return prodRisklvl;
    }

    public void setProdRisklvl(String prodRisklvl) {
        this.prodRisklvl = prodRisklvl;
    }

    public boolean isPeriodical() {
        return periodical;
    }

    public void setPeriodical(boolean periodical) {
        this.periodical = periodical;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public BigDecimal getBuyStartingAmount() {
        return buyStartingAmount;
    }

    public void setBuyStartingAmount(BigDecimal buyStartingAmount) {
        this.buyStartingAmount = buyStartingAmount;
    }

    public BigDecimal getAppendStartingAmount() {
        return appendStartingAmount;
    }

    public void setAppendStartingAmount(BigDecimal appendStartingAmount) {
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public String getRiskMsg() {
        return riskMsg;
    }

    public void setRiskMsg(String riskMsg) {
        this.riskMsg = riskMsg;
    }
}
