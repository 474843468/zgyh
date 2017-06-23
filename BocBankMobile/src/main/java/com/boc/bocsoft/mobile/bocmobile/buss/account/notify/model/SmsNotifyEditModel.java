package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/16.
 * 短信通知界面数据模型
 */
public class SmsNotifyEditModel implements Parcelable {
    //签约账户
    private AccountBean signAccount;
    //接受短信的手机
    private String phoneNum;
    //最小通知金额
    private String minMoney = "0";
    //最大通知金额
    private String maxMoney = "99999999999.99";
    //缴费银行账户
    private AccountBean feeAccount;
    //缴费费率
    private String feeRate;
    //手机验证码
    private String verifyCode;
    //会话ID
    private String conversitionId;
    //ServiceID
    private String serviceId;
    //用户名称
    private String userName;
    //是否开通短信提醒
    private boolean status = true;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AccountBean getSignAccount() {
        return signAccount;
    }

    public void setSignAccount(AccountBean signAccount) {
        this.signAccount = signAccount;
    }

    public SmsNotifyEditModel() {

    }

    public SmsNotifyEditModel(SmsNotifyModel model) {
        signAccount = model.getSignAccount();
        phoneNum = model.getPhoneNum();
        minMoney = model.getMinMoney();
        maxMoney = model.getMaxMoney();
        if (maxMoney.equals("0") || maxMoney.equals("0.00")) {
            maxMoney = "99999999999.99";
        }
        feeAccount = model.getFeeAccount();
        feeRate = model.getFeeRate();
        userName = model.getUserName();
    }

    public SmsNotifyEditModel(SmsNotifyEditModel model) {
        signAccount = model.getSignAccount();
        phoneNum = model.getPhoneNum();
        minMoney = model.getMinMoney();
        maxMoney = model.getMaxMoney();
        if (maxMoney.equals("0") || maxMoney.equals("0.00")) {
            maxMoney = "99999999999.99";
        }
        feeAccount = model.getFeeAccount();
        feeRate = model.getFeeRate();
        userName = model.getUserName();
        status = model.isStatus();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getConversitionId() {
        return conversitionId;
    }

    public void setConversitionId(String conversitionId) {
        this.conversitionId = conversitionId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }

    public String getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }

    public AccountBean getFeeAccount() {
        return feeAccount;
    }

    public void setFeeAccount(AccountBean feeAccount) {
        this.feeAccount = feeAccount;
    }

    public void setFeeAccount(String feeAccount) {
        AccountBean account = new AccountBean();
        account.setAccountNumber(feeAccount);
        this.feeAccount = account;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.signAccount, flags);
        dest.writeString(this.phoneNum);
        dest.writeString(this.minMoney);
        dest.writeString(this.maxMoney);
        dest.writeParcelable(this.feeAccount, flags);
        dest.writeString(this.feeRate);
        dest.writeString(this.verifyCode);
        dest.writeString(this.conversitionId);
        dest.writeString(this.serviceId);
        dest.writeString(this.userName);
        dest.writeByte(status ? (byte) 1 : (byte) 0);
    }

    protected SmsNotifyEditModel(Parcel in) {
        this.signAccount = in.readParcelable(AccountBean.class.getClassLoader());
        this.phoneNum = in.readString();
        this.minMoney = in.readString();
        this.maxMoney = in.readString();
        this.feeAccount = in.readParcelable(AccountBean.class.getClassLoader());
        this.feeRate = in.readString();
        this.verifyCode = in.readString();
        this.conversitionId = in.readString();
        this.serviceId = in.readString();
        this.userName = in.readString();
        this.status = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SmsNotifyEditModel> CREATOR = new Parcelable.Creator<SmsNotifyEditModel>() {
        @Override
        public SmsNotifyEditModel createFromParcel(Parcel source) {
            return new SmsNotifyEditModel(source);
        }

        @Override
        public SmsNotifyEditModel[] newArray(int size) {
            return new SmsNotifyEditModel[size];
        }
    };
}
