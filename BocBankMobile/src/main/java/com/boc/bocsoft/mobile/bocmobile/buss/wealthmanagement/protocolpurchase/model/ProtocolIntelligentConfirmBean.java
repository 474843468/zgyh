package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 4.61 061 智能协议申请预交易--响应
 * Created by liuweidong on 2016/11/9.
 */
public class ProtocolIntelligentConfirmBean implements Parcelable {
    private String accNo;//	银行帐号
    private String agrCode;// 产品协议
    private String agrName;// 协议名称
    private String periodPur;//	购买周期
    private String firstDatePur;// 下次购买日
    private String periodRed;//	赎回周期
    private String firstDateRed;// 下次赎回日
    private String amount;// 单期购买金额/单次购买金额
    private String minAmount;//	账户保留余额
    private String maxAmount;//	最大购买金额
    private String periodAgr;//	协议周期
    private String isControl;//	是否不限期
    private String totalPeriod;// 签约期数
    private String endDate;// 协议到期日
    private String lastDays;// 协议总持续天数
    private String willPurCount;// 预计剩余购买次数
    private String willRedCount;// 预计剩余赎回次数
    private String eachpbalDays;// 每期持有天数
    private String oneTmredAmt;// 单次赎回金额
    private String failMax;// 失败次数上限
    private String isNeedPur;// 购买选项
    private String isNeedRed;//	赎回选项

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getPeriodAgr() {
        return periodAgr;
    }

    public void setPeriodAgr(String periodAgr) {
        this.periodAgr = periodAgr;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLastDays() {
        return lastDays;
    }

    public void setLastDays(String lastDays) {
        this.lastDays = lastDays;
    }

    public String getWillPurCount() {
        return willPurCount;
    }

    public void setWillPurCount(String willPurCount) {
        this.willPurCount = willPurCount;
    }

    public String getWillRedCount() {
        return willRedCount;
    }

    public void setWillRedCount(String willRedCount) {
        this.willRedCount = willRedCount;
    }

    public String getEachpbalDays() {
        return eachpbalDays;
    }

    public void setEachpbalDays(String eachpbalDays) {
        this.eachpbalDays = eachpbalDays;
    }

    public String getOneTmredAmt() {
        return oneTmredAmt;
    }

    public void setOneTmredAmt(String oneTmredAmt) {
        this.oneTmredAmt = oneTmredAmt;
    }

    public String getFailMax() {
        return failMax;
    }

    public void setFailMax(String failMax) {
        this.failMax = failMax;
    }

    public String getIsNeedPur() {
        return isNeedPur;
    }

    public void setIsNeedPur(String isNeedPur) {
        this.isNeedPur = isNeedPur;
    }

    public String getIsNeedRed() {
        return isNeedRed;
    }

    public void setIsNeedRed(String isNeedRed) {
        this.isNeedRed = isNeedRed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accNo);
        dest.writeString(this.agrCode);
        dest.writeString(this.agrName);
        dest.writeString(this.periodPur);
        dest.writeString(this.firstDatePur);
        dest.writeString(this.periodRed);
        dest.writeString(this.firstDateRed);
        dest.writeString(this.amount);
        dest.writeString(this.minAmount);
        dest.writeString(this.maxAmount);
        dest.writeString(this.periodAgr);
        dest.writeString(this.isControl);
        dest.writeString(this.totalPeriod);
        dest.writeString(this.endDate);
        dest.writeString(this.lastDays);
        dest.writeString(this.willPurCount);
        dest.writeString(this.willRedCount);
        dest.writeString(this.eachpbalDays);
        dest.writeString(this.oneTmredAmt);
        dest.writeString(this.failMax);
        dest.writeString(this.isNeedPur);
        dest.writeString(this.isNeedRed);
    }

    public ProtocolIntelligentConfirmBean() {
    }

    protected ProtocolIntelligentConfirmBean(Parcel in) {
        this.accNo = in.readString();
        this.agrCode = in.readString();
        this.agrName = in.readString();
        this.periodPur = in.readString();
        this.firstDatePur = in.readString();
        this.periodRed = in.readString();
        this.firstDateRed = in.readString();
        this.amount = in.readString();
        this.minAmount = in.readString();
        this.maxAmount = in.readString();
        this.periodAgr = in.readString();
        this.isControl = in.readString();
        this.totalPeriod = in.readString();
        this.endDate = in.readString();
        this.lastDays = in.readString();
        this.willPurCount = in.readString();
        this.willRedCount = in.readString();
        this.eachpbalDays = in.readString();
        this.oneTmredAmt = in.readString();
        this.failMax = in.readString();
        this.isNeedPur = in.readString();
        this.isNeedRed = in.readString();
    }

    public static final Parcelable.Creator<ProtocolIntelligentConfirmBean> CREATOR = new Parcelable.Creator<ProtocolIntelligentConfirmBean>() {
        @Override
        public ProtocolIntelligentConfirmBean createFromParcel(Parcel source) {
            return new ProtocolIntelligentConfirmBean(source);
        }

        @Override
        public ProtocolIntelligentConfirmBean[] newArray(int size) {
            return new ProtocolIntelligentConfirmBean[size];
        }
    };
}
