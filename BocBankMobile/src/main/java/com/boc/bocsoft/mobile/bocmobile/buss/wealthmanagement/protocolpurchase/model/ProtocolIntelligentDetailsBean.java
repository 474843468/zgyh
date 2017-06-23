package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 4.74 074 智能协议详情--响应
 * Created by liuweidong on 2016/11/8.
 */
public class ProtocolIntelligentDetailsBean implements Parcelable {
    private String accNo;//	银行帐号
    private String productCode;// 产品代码
    private String productName;// 产品名称
    private String agrType;// 协议类型 1：智能投资2：定时定额投资3：周期滚续投资4：余额理财投资
    private String investType;// 投资方式 1:周期连续协议2:周期不连续协议3:多次购买协议4:多次赎回协议
    private String agrCode;// 协议代码
    private String agrName;// 协议名称
    private String agrPeriod;//	协议总期数
    private String agrCurrPeriod;//	协议当前期数
    private String periodAgr;//	投资周期
    private String minInvestPeriod;// 最小投资期数
    private String singleInvestPeriod;// 单周期投资期数 当为周期连续和不连续此域有值，其他则为0
    private String periodPur;//	购买周期
    private String firstDatePur;// 下次购买日
    private String periodRed;//	赎回周期
    private String firstDateRed;// 下次赎回日
    private String rate;// 预计年收益率（%）
    private String agrPurStart;// 协议投资起点金额
    private String rateDetail;// 预计年收益率（%）(最大值)
    private String memo;// 协议介绍信息
    private String isNeedRed;//	赎回频率 0：期末赎回 1：不赎回 当协议为多次够购买时，此域有意义
    private String isNeedPur;//	购买频率 0：期初申购 1：不申购 当协议为多次赎回时，此域有意义
    private String isQuota;// 是否允许不定额方式 0：不允许1：允许
    private String kind;// 产品性质 0:结构性理财产品1:类基金理财产品

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getAgrPeriod() {
        return agrPeriod;
    }

    public void setAgrPeriod(String agrPeriod) {
        this.agrPeriod = agrPeriod;
    }

    public String getAgrCurrPeriod() {
        return agrCurrPeriod;
    }

    public void setAgrCurrPeriod(String agrCurrPeriod) {
        this.agrCurrPeriod = agrCurrPeriod;
    }

    public String getPeriodAgr() {
        return periodAgr;
    }

    public void setPeriodAgr(String periodAgr) {
        this.periodAgr = periodAgr;
    }

    public String getMinInvestPeriod() {
        return minInvestPeriod;
    }

    public void setMinInvestPeriod(String minInvestPeriod) {
        this.minInvestPeriod = minInvestPeriod;
    }

    public String getSingleInvestPeriod() {
        return singleInvestPeriod;
    }

    public void setSingleInvestPeriod(String singleInvestPeriod) {
        this.singleInvestPeriod = singleInvestPeriod;
    }

    public String getPeriodPur() {
        return periodPur;
    }

    public void setPeriodPur(String periodPur) {
        this.periodPur = periodPur;
    }

    public String getFirstDatePur() {
        return firstDatePur;
    }

    public void setFirstDatePur(String firstDatePur) {
        this.firstDatePur = firstDatePur;
    }

    public String getPeriodRed() {
        return periodRed;
    }

    public void setPeriodRed(String periodRed) {
        this.periodRed = periodRed;
    }

    public String getFirstDateRed() {
        return firstDateRed;
    }

    public void setFirstDateRed(String firstDateRed) {
        this.firstDateRed = firstDateRed;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAgrPurStart() {
        return agrPurStart;
    }

    public void setAgrPurStart(String agrPurStart) {
        this.agrPurStart = agrPurStart;
    }

    public String getRateDetail() {
        return rateDetail;
    }

    public void setRateDetail(String rateDetail) {
        this.rateDetail = rateDetail;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIsNeedRed() {
        return isNeedRed;
    }

    public void setIsNeedRed(String isNeedRed) {
        this.isNeedRed = isNeedRed;
    }

    public String getIsNeedPur() {
        return isNeedPur;
    }

    public void setIsNeedPur(String isNeedPur) {
        this.isNeedPur = isNeedPur;
    }

    public String getIsQuota() {
        return isQuota;
    }

    public void setIsQuota(String isQuota) {
        this.isQuota = isQuota;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accNo);
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeString(this.agrType);
        dest.writeString(this.investType);
        dest.writeString(this.agrCode);
        dest.writeString(this.agrName);
        dest.writeString(this.agrPeriod);
        dest.writeString(this.agrCurrPeriod);
        dest.writeString(this.periodAgr);
        dest.writeString(this.minInvestPeriod);
        dest.writeString(this.singleInvestPeriod);
        dest.writeString(this.periodPur);
        dest.writeString(this.firstDatePur);
        dest.writeString(this.periodRed);
        dest.writeString(this.firstDateRed);
        dest.writeString(this.rate);
        dest.writeString(this.agrPurStart);
        dest.writeString(this.rateDetail);
        dest.writeString(this.memo);
        dest.writeString(this.isNeedRed);
        dest.writeString(this.isNeedPur);
        dest.writeString(this.isQuota);
        dest.writeString(this.kind);
    }

    public ProtocolIntelligentDetailsBean() {
    }

    protected ProtocolIntelligentDetailsBean(Parcel in) {
        this.accNo = in.readString();
        this.productCode = in.readString();
        this.productName = in.readString();
        this.agrType = in.readString();
        this.investType = in.readString();
        this.agrCode = in.readString();
        this.agrName = in.readString();
        this.agrPeriod = in.readString();
        this.agrCurrPeriod = in.readString();
        this.periodAgr = in.readString();
        this.minInvestPeriod = in.readString();
        this.singleInvestPeriod = in.readString();
        this.periodPur = in.readString();
        this.firstDatePur = in.readString();
        this.periodRed = in.readString();
        this.firstDateRed = in.readString();
        this.rate = in.readString();
        this.agrPurStart = in.readString();
        this.rateDetail = in.readString();
        this.memo = in.readString();
        this.isNeedRed = in.readString();
        this.isNeedPur = in.readString();
        this.isQuota = in.readString();
        this.kind = in.readString();
    }

    public static final Parcelable.Creator<ProtocolIntelligentDetailsBean> CREATOR = new Parcelable.Creator<ProtocolIntelligentDetailsBean>() {
        @Override
        public ProtocolIntelligentDetailsBean createFromParcel(Parcel source) {
            return new ProtocolIntelligentDetailsBean(source);
        }

        @Override
        public ProtocolIntelligentDetailsBean[] newArray(int size) {
            return new ProtocolIntelligentDetailsBean[size];
        }
    };
}
