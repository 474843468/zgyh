package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeParamsData;

/**
 * 作者：XieDu
 * 创建时间：2016/8/15 22:50
 * 描述：
 */
public class LoanDepositMultipleQueryBean implements IPledgeParamsData, Parcelable {
    /**
     * 汇率
     */
    private String exchangeRate;
    /**
     * 浮动比
     */
    private String floatingRate;
    /**
     * 浮动值
     */
    private String floatingValue;
    /**
     * 质押率(人民币)
     */
    private String pledgeRate_ZH;
    /**
     * 质押率(美元)
     */
    private String pledgeRate_US;
    /**
     * 质押率(其他)
     */
    private String pledgeRate_OT;
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

    private String conversationId;

    public String getExchangeRate() { return exchangeRate;}

    public void setExchangeRate(String exchangeRate) { this.exchangeRate = exchangeRate;}

    public String getFloatingRate() { return floatingRate;}

    public void setFloatingRate(String floatingRate) { this.floatingRate = floatingRate;}

    public String getFloatingValue() { return floatingValue;}

    public void setFloatingValue(String floatingValue) { this.floatingValue = floatingValue;}

    public String getPledgeRate_ZH() { return pledgeRate_ZH;}

    public void setPledgeRate_ZH(String pledgeRate_ZH) { this.pledgeRate_ZH = pledgeRate_ZH;}

    public String getPledgeRate_US() { return pledgeRate_US;}

    public void setPledgeRate_US(String pledgeRate_US) { this.pledgeRate_US = pledgeRate_US;}

    public String getPledgeRate_OT() { return pledgeRate_OT;}

    public void setPledgeRate_OT(String pledgeRate_OT) { this.pledgeRate_OT = pledgeRate_OT;}

    public String getLoanPeriodMax() { return loanPeriodMax;}

    public void setLoanPeriodMax(String loanPeriodMax) { this.loanPeriodMax = loanPeriodMax;}

    public String getLoanPeriodMin() { return loanPeriodMin;}

    public void setLoanPeriodMin(String loanPeriodMin) { this.loanPeriodMin = loanPeriodMin;}

    public String getSingleQuotaMax() { return singleQuotaMax;}

    public void setSingleQuotaMax(String singleQuotaMax) { this.singleQuotaMax = singleQuotaMax;}

    public String getSingleQuotaMin() { return singleQuotaMin;}

    public void setSingleQuotaMin(String singleQuotaMin) { this.singleQuotaMin = singleQuotaMin;}

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exchangeRate);
        dest.writeString(this.floatingRate);
        dest.writeString(this.floatingValue);
        dest.writeString(this.pledgeRate_ZH);
        dest.writeString(this.pledgeRate_US);
        dest.writeString(this.pledgeRate_OT);
        dest.writeString(this.loanPeriodMax);
        dest.writeString(this.loanPeriodMin);
        dest.writeString(this.singleQuotaMax);
        dest.writeString(this.singleQuotaMin);
        dest.writeString(this.conversationId);
    }

    public LoanDepositMultipleQueryBean() {}

    protected LoanDepositMultipleQueryBean(Parcel in) {
        this.exchangeRate = in.readString();
        this.floatingRate = in.readString();
        this.floatingValue = in.readString();
        this.pledgeRate_ZH = in.readString();
        this.pledgeRate_US = in.readString();
        this.pledgeRate_OT = in.readString();
        this.loanPeriodMax = in.readString();
        this.loanPeriodMin = in.readString();
        this.singleQuotaMax = in.readString();
        this.singleQuotaMin = in.readString();
        this.conversationId = in.readString();
    }

    public static final Parcelable.Creator<LoanDepositMultipleQueryBean> CREATOR =
            new Parcelable.Creator<LoanDepositMultipleQueryBean>() {
                @Override
                public LoanDepositMultipleQueryBean createFromParcel(
                        Parcel source) {return new LoanDepositMultipleQueryBean(source);}

                @Override
                public LoanDepositMultipleQueryBean[] newArray(
                        int size) {return new LoanDepositMultipleQueryBean[size];}
            };
}
