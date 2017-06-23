package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeParamsData;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 14:00
 * 描述：
 */
public class FinancePledgeParamsData implements IPledgeParamsData, Parcelable {

    private String conversationId;

    private String loanType;

    /**
     * 贷款期限上限
     */
    private String loanPeriodMax;
    /**
     * 贷款期限下限
     */
    private String loanPeriodMin;
    /**
     * 单笔限额上限
     */
    private String singleQuotaMax;
    /**
     * 单笔限额下限
     */
    private String singleQuotaMin;

    /**
     * 当日剩余可用金额
     */
    private String availableAmtToday;
    /**
     * 浮动比
     */
    private String floatingRate;
    /**
     * 浮动值
     */
    private String floatingValue;

    @Override
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    @Override
    public String getLoanPeriodMax() {
        return loanPeriodMax;
    }

    public void setLoanPeriodMax(String loanPeriodMax) {
        this.loanPeriodMax = loanPeriodMax;
    }

    @Override
    public String getLoanPeriodMin() {
        return loanPeriodMin;
    }

    public void setLoanPeriodMin(String loanPeriodMin) {
        this.loanPeriodMin = loanPeriodMin;
    }

    @Override
    public String getSingleQuotaMax() {
        return singleQuotaMax;
    }

    public void setSingleQuotaMax(String singleQuotaMax) {
        this.singleQuotaMax = singleQuotaMax;
    }

    @Override
    public String getSingleQuotaMin() {
        return singleQuotaMin;
    }

    public void setSingleQuotaMin(String singleQuotaMin) {
        this.singleQuotaMin = singleQuotaMin;
    }

    @Override
    public String getFloatingRate() {
        return floatingRate;
    }

    public void setFloatingRate(String floatingRate) {
        this.floatingRate = floatingRate;
    }

    @Override
    public String getFloatingValue() {
        return floatingValue;
    }

    public void setFloatingValue(String floatingValue) {
        this.floatingValue = floatingValue;
    }

    public String getAvailableAmtToday() {
        return availableAmtToday;
    }

    public void setAvailableAmtToday(String availableAmtToday) {
        this.availableAmtToday = availableAmtToday;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationId);
        dest.writeString(this.loanType);
        dest.writeString(this.loanPeriodMax);
        dest.writeString(this.loanPeriodMin);
        dest.writeString(this.singleQuotaMax);
        dest.writeString(this.singleQuotaMin);
        dest.writeString(this.availableAmtToday);
        dest.writeString(this.floatingRate);
        dest.writeString(this.floatingValue);
    }

    public FinancePledgeParamsData() {}

    protected FinancePledgeParamsData(Parcel in) {
        this.conversationId = in.readString();
        this.loanType = in.readString();
        this.loanPeriodMax = in.readString();
        this.loanPeriodMin = in.readString();
        this.singleQuotaMax = in.readString();
        this.singleQuotaMin = in.readString();
        this.availableAmtToday = in.readString();
        this.floatingRate = in.readString();
        this.floatingValue = in.readString();
    }

    public static final Parcelable.Creator<FinancePledgeParamsData> CREATOR =
            new Parcelable.Creator<FinancePledgeParamsData>() {
                @Override
                public FinancePledgeParamsData createFromParcel(
                        Parcel source) {return new FinancePledgeParamsData(source);}

                @Override
                public FinancePledgeParamsData[] newArray(
                        int size) {return new FinancePledgeParamsData[size];}
            };
}
