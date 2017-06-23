package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/**
 * 理财产品列表（登录前与登录后）
 * Created by liuweidong on 2016/9/20.
 */
public class WealthListBean implements Serializable, Parcelable {
    /**公共部分*/
    private String prodName;// 产品名称
    private String prodCode;// 产品代码
    private String curCode;// 产品币种
    private String yearlyRR;// 预计年收益率（%）
    private String applyObj;// 适用对象
    private String status;// 产品销售状态
    private String prodRisklvl;// 产品风险级别
    private String periodical;// 是否周期性产品
    private String remainCycleCount = "";// 剩余可购买最大期数.非周期性产品返回空
    private String impawnPermit;// 是否可组合购买
    private String progressionflag;// 是否收益累计产品
    private String price;// 最新净值
    private String issueType;// 产品类型
    private String termType;// 产品期限特性
    private String priceDate;// 净值日期
    private String isLockPeriod;// 是否为业绩基准产品
    private String isBuy;// 是否显示购买连接
    private String isAgreement;// 是否允许投资协议申请
    private String isProfiTest;// 是否可收益试算
    private String rateDetail;// 预计年收益率（%）(最大值)
    /**登录前*/
    private String prodTimeLimit;// 产品期限
    private String buyPrice;// 购买价格
    private String subAmount;// 起购金额
    /**登录后*/
    private String productKind;// 产品性质
    private String periedTime;// 产品期限（产品到日期减去产品起息日）
    private String accumulatePrice;// 累计净值
    private String subPayAmt;// 购买起点金额
    private String availableAmt;// 剩余额度

    public WealthListBean() {
    }

    public WealthListBean(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(String subAmount) {
        this.subAmount = subAmount;
    }

    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public String getPeriedTime() {
        return periedTime;
    }

    public void setPeriedTime(String periedTime) {
        this.periedTime = periedTime;
    }

    public String getYearlyRR() {
        return yearlyRR;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
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

    public String getPeriodical() {
        return periodical;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
    }

    public String getImpawnPermit() {
        return impawnPermit;
    }

    public void setImpawnPermit(String impawnPermit) {
        this.impawnPermit = impawnPermit;
    }

    public String getProgressionflag() {
        return progressionflag;
    }

    public void setProgressionflag(String progressionflag) {
        this.progressionflag = progressionflag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }

    public String getIsProfiTest() {
        return isProfiTest;
    }

    public void setIsProfiTest(String isProfiTest) {
        this.isProfiTest = isProfiTest;
    }

    public String getRateDetail() {
        return rateDetail;
    }

    public void setRateDetail(String rateDetail) {
        this.rateDetail = rateDetail;
    }

    public String getAccumulatePrice() {
        return accumulatePrice;
    }

    public void setAccumulatePrice(String accumulatePrice) {
        this.accumulatePrice = accumulatePrice;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getSubPayAmt() {
        return subPayAmt;
    }

    public void setSubPayAmt(String subPayAmt) {
        this.subPayAmt = subPayAmt;
    }

    public String getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(String availableAmt) {
        this.availableAmt = availableAmt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WealthListBean that = (WealthListBean) o;

        return prodCode != null ? prodCode.equals(that.prodCode) : that.prodCode == null;

    }

    @Override
    public int hashCode() {
        return prodCode != null ? prodCode.hashCode() : 0;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prodName);
        dest.writeString(this.prodCode);
        dest.writeString(this.curCode);
        dest.writeString(this.yearlyRR);
        dest.writeString(this.applyObj);
        dest.writeString(this.status);
        dest.writeString(this.prodRisklvl);
        dest.writeString(this.periodical);
        dest.writeString(this.remainCycleCount);
        dest.writeString(this.impawnPermit);
        dest.writeString(this.progressionflag);
        dest.writeString(this.price);
        dest.writeString(this.issueType);
        dest.writeString(this.termType);
        dest.writeString(this.priceDate);
        dest.writeString(this.isLockPeriod);
        dest.writeString(this.isBuy);
        dest.writeString(this.isAgreement);
        dest.writeString(this.isProfiTest);
        dest.writeString(this.rateDetail);
        dest.writeString(this.prodTimeLimit);
        dest.writeString(this.buyPrice);
        dest.writeString(this.subAmount);
        dest.writeString(this.productKind);
        dest.writeString(this.periedTime);
        dest.writeString(this.accumulatePrice);
        dest.writeString(this.subPayAmt);
        dest.writeString(this.availableAmt);
    }

    protected WealthListBean(Parcel in) {
        this.prodName = in.readString();
        this.prodCode = in.readString();
        this.curCode = in.readString();
        this.yearlyRR = in.readString();
        this.applyObj = in.readString();
        this.status = in.readString();
        this.prodRisklvl = in.readString();
        this.periodical = in.readString();
        this.remainCycleCount = in.readString();
        this.impawnPermit = in.readString();
        this.progressionflag = in.readString();
        this.price = in.readString();
        this.issueType = in.readString();
        this.termType = in.readString();
        this.priceDate = in.readString();
        this.isLockPeriod = in.readString();
        this.isBuy = in.readString();
        this.isAgreement = in.readString();
        this.isProfiTest = in.readString();
        this.rateDetail = in.readString();
        this.prodTimeLimit = in.readString();
        this.buyPrice = in.readString();
        this.subAmount = in.readString();
        this.productKind = in.readString();
        this.periedTime = in.readString();
        this.accumulatePrice = in.readString();
        this.subPayAmt = in.readString();
        this.availableAmt = in.readString();
    }

    public static final Parcelable.Creator<WealthListBean> CREATOR =
            new Parcelable.Creator<WealthListBean>() {
                @Override
                public WealthListBean createFromParcel(Parcel source) {
                    return new WealthListBean(source);
                }

                @Override
                public WealthListBean[] newArray(int size) {return new WealthListBean[size];}
            };
}
