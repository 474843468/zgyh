package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeLoanInfoFillViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.NotConvert;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 14:05
 * 描述：
 */
public class PledgeFinanceInfoFillViewModel implements IPledgeLoanInfoFillViewModel{
    /**
     * 安全工具名称
     */
    @NotConvert
    private String combinName;
    /**
     * 安全工具组合
     */
    private String _combinId;
    /**
     * 贷款品种	String	M	固定值为“PLEA”
     */
    private String loanType;
    /**
     * 可用金额
     */
    private String availableAmount;
    /**
     * 本次用款金额
     */
    private String amount;
    private String currencyCode;
    private String loanPeriod;
    private String loanRate;
    /**
     * 还款方式
     * 1：到期一次性还本付息
     * 2：按期还息到期还本
     * 贷款期限6个月（含）以下，还款方式字段显示为“到期一次性还本付息”；
     * 贷款期限6个月以上，还款方式显示为“按期还息到期还本”
     */
    private String payType;
    /**
     * 还款周期
     * 1：到期时
     * 2：按月
     * 贷款期限6个月（含）以下，还款周期显示为“到期时”；
     * 贷款期限6个月以上，还款周期显示为“按月”
     */
    private String payCycle;
    /**
     * 收款账户
     */
    private String toActNum;
    /**
     * 收款账户Id
     */
    private String toAccount;
    /**
     * 还款账户Id
     */
    private String payAccount;
    /**
     * 需与PsnLOANMultipleQuery、PsnLOANPledgeSubmit接口保持一致
     */
    private String conversationId;

    @NotConvert
    private String payActNum;

    private String pledgeRate;

    public String get_combinId() { return _combinId;}

    public void set_combinId(String _combinId) { this._combinId = _combinId;}

    public String getLoanType() { return loanType;}

    public void setLoanType(String loanType) { this.loanType = loanType;}

    public String getAvailableAmount() { return availableAmount;}

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAmount() { return amount;}

    public void setAmount(String amount) { this.amount = amount;}

    public String getCurrencyCode() { return currencyCode;}

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}

    public String getLoanPeriod() { return loanPeriod;}

    public void setLoanPeriod(String loanPeriod) { this.loanPeriod = loanPeriod;}

    public String getLoanRate() { return loanRate;}

    public void setLoanRate(String loanRate) { this.loanRate = loanRate;}

    public String getPayType() { return payType;}

    public void setPayType(String payType) { this.payType = payType;}

    public String getPayCycle() { return payCycle;}

    public void setPayCycle(String payCycle) { this.payCycle = payCycle;}

    public String getToActNum() { return toActNum;}

    public void setToActNum(String toActNum) { this.toActNum = toActNum;}

    public String getToAccount() { return toAccount;}

    public void setToAccount(String toAccount) { this.toAccount = toAccount;}

    public String getPayAccount() { return payAccount;}

    public void setPayAccount(String payAccount) { this.payAccount = payAccount;}

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}

    public String getPayActNum() {
        return payActNum;
    }

    public void setPayActNum(String payActNum) {
        this.payActNum = payActNum;
    }

    public String getPayTypeString() {
        return ApplicationContext.getInstance()
                                 .getString(Integer.valueOf(loanPeriod) <= 6
                                         ? R.string.boc_pledge_info_repay_type1
                                         : R.string.boc_pledge_info_repay_type2);
    }

    @Override
    public String getPayCycleString() {
        return ApplicationContext.getInstance()
                                 .getString(Integer.valueOf(loanPeriod) <= 6
                                         ? R.string.boc_pledge_finance_paycycle1
                                         : R.string.boc_pledge_finance_paycycle2);
    }

    public String getCombinName() {
        return combinName;
    }

    public void setCombinName(String combinName) {
        this.combinName = combinName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.combinName);
        dest.writeString(this._combinId);
        dest.writeString(this.loanType);
        dest.writeString(this.availableAmount);
        dest.writeString(this.amount);
        dest.writeString(this.currencyCode);
        dest.writeString(this.loanPeriod);
        dest.writeString(this.loanRate);
        dest.writeString(this.payType);
        dest.writeString(this.payCycle);
        dest.writeString(this.toActNum);
        dest.writeString(this.toAccount);
        dest.writeString(this.payAccount);
        dest.writeString(this.conversationId);
        dest.writeString(this.payActNum);
    }

    public PledgeFinanceInfoFillViewModel() {}

    protected PledgeFinanceInfoFillViewModel(Parcel in) {
        this.combinName = in.readString();
        this._combinId = in.readString();
        this.loanType = in.readString();
        this.availableAmount = in.readString();
        this.amount = in.readString();
        this.currencyCode = in.readString();
        this.loanPeriod = in.readString();
        this.loanRate = in.readString();
        this.payType = in.readString();
        this.payCycle = in.readString();
        this.toActNum = in.readString();
        this.toAccount = in.readString();
        this.payAccount = in.readString();
        this.conversationId = in.readString();
        this.payActNum = in.readString();
    }

    public static final Parcelable.Creator<PledgeFinanceInfoFillViewModel> CREATOR =
            new Parcelable.Creator<PledgeFinanceInfoFillViewModel>() {
                @Override
                public PledgeFinanceInfoFillViewModel createFromParcel(
                        Parcel source) {return new PledgeFinanceInfoFillViewModel(source);}

                @Override
                public PledgeFinanceInfoFillViewModel[] newArray(
                        int size) {return new PledgeFinanceInfoFillViewModel[size];}
            };

    public String getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(String pledgeRate) {
        this.pledgeRate = pledgeRate;
    }
}
