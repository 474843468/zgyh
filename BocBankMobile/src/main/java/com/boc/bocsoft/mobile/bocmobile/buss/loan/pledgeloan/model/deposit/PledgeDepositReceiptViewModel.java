package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 作者：XieDu
 * 创建时间：2016/8/15 22:37
 * 描述：
 */
public class PledgeDepositReceiptViewModel implements Parcelable {
    private BigDecimal availableAmount;//可贷额度总数
    private String accountId;
    /**
     * 账号
     */
    private String accountNumber;
    private ArrayList<PersonalTimeAccountBean> personalTimeAccountBeanArrayList;
    private LoanDepositMultipleQueryBean loanDepositMultipleQueryBean;

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public ArrayList<PersonalTimeAccountBean> getPersonalTimeAccountBeanArrayList() {
        return personalTimeAccountBeanArrayList;
    }

    public void setPersonalTimeAccountBeanArrayList(
            ArrayList<PersonalTimeAccountBean> personalTimeAccountBeanArrayList) {
        this.personalTimeAccountBeanArrayList = personalTimeAccountBeanArrayList;
    }

    public LoanDepositMultipleQueryBean getLoanDepositMultipleQueryBean() {
        return loanDepositMultipleQueryBean;
    }

    public void setLoanDepositMultipleQueryBean(
            LoanDepositMultipleQueryBean loanDepositMultipleQueryBean) {
        this.loanDepositMultipleQueryBean = loanDepositMultipleQueryBean;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.availableAmount);
        dest.writeString(this.accountId);
        dest.writeString(this.accountNumber);
        dest.writeTypedList(this.personalTimeAccountBeanArrayList);
        dest.writeParcelable(this.loanDepositMultipleQueryBean, flags);
    }

    public PledgeDepositReceiptViewModel() {}

    protected PledgeDepositReceiptViewModel(Parcel in) {
        this.availableAmount = (BigDecimal) in.readSerializable();
        this.accountId = in.readString();
        this.accountNumber = in.readString();
        this.personalTimeAccountBeanArrayList =
                in.createTypedArrayList(PersonalTimeAccountBean.CREATOR);
        this.loanDepositMultipleQueryBean =
                in.readParcelable(LoanDepositMultipleQueryBean.class.getClassLoader());
    }

    public static final Creator<PledgeDepositReceiptViewModel> CREATOR =
            new Creator<PledgeDepositReceiptViewModel>() {
                @Override
                public PledgeDepositReceiptViewModel createFromParcel(
                        Parcel source) {return new PledgeDepositReceiptViewModel(source);}

                @Override
                public PledgeDepositReceiptViewModel[] newArray(
                        int size) {return new PledgeDepositReceiptViewModel[size];}
            };
}
