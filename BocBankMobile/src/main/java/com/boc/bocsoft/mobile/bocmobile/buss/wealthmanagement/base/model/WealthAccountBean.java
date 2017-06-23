package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.io.Serializable;

/**
 * 理财账户信息
 * Created by liuweidong on 2016/9/24.
 */
public class WealthAccountBean extends AccountBean implements Parcelable ,Serializable{

    private String accountNo;// 资金账号
    private String bancID;// 账户开户行 核心系统中的账号开户行
    private String xpadAccountSatus;// 账户状态 0：停用1：可用
    private String ibkNumber;// 省行联行号
    private String xpadAccount;// 客户理财账户
    private String accountKey;// 账户缓存标识 所有账户均返回，客户非敏感数据

    public WealthAccountBean(String accountId, String accountNum,String accountKey) {
        setAccountId(accountId);
        setAccountNumber(accountNum);
        accountNo = accountNum;
        this.accountKey = accountKey;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
        setAccountNumber(accountNo);
    }

    public String getBancID() {
        return bancID;
    }

    public void setBancID(String bancID) {
        this.bancID = bancID;
    }

    public String getXpadAccountSatus() {
        return xpadAccountSatus;
    }

    public void setXpadAccountSatus(String xpadAccountSatus) {
        this.xpadAccountSatus = xpadAccountSatus;
    }

    public String getIbkNumber() {
        return ibkNumber;
    }

    public void setIbkNumber(String ibkNumber) {
        this.ibkNumber = ibkNumber;
    }

    public String getXpadAccount() {
        return xpadAccount;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if(getClass() != o.getClass() && !o.getClass().isAssignableFrom(getClass()))
            return false;


        AccountBean that = (AccountBean) o;

        return getAccountId() != null ? getAccountId().equals(that.getAccountId()) : that.getAccountId() == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNo);
        dest.writeString(this.bancID);
        dest.writeString(this.xpadAccountSatus);
        dest.writeString(this.ibkNumber);
        dest.writeString(this.xpadAccount);
        dest.writeString(this.accountKey);
        dest.writeString(this.getAccountId());
        dest.writeString(this.getAccountType());
    }

    public WealthAccountBean() {
    }

    protected WealthAccountBean(Parcel in) {
        this.accountNo = in.readString();
        this.bancID = in.readString();
        this.xpadAccountSatus = in.readString();
        this.ibkNumber = in.readString();
        this.xpadAccount = in.readString();
        this.accountKey = in.readString();
        setAccountId(in.readString());
        setAccountType(in.readString());
    }

    public static final Parcelable.Creator<WealthAccountBean> CREATOR = new Parcelable.Creator<WealthAccountBean>() {
        @Override
        public WealthAccountBean createFromParcel(Parcel source) {
            return new WealthAccountBean(source);
        }

        @Override
        public WealthAccountBean[] newArray(int size) {
            return new WealthAccountBean[size];
        }
    };
}
