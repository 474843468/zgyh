package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

/**
 * Created by wangtong on 2016/6/16.
 * 短信通知详情页面
 */
public class SmsNotifyModel implements Parcelable {

    //签约账户
    private AccountBean signAccount;
    //用户姓名
    private String userName;
    //接收通知的手机
    private String phoneNum;
    //最小通知金额
    private String minMoney;
    //最大通知金额
    private String maxMoney;
    //动户范围
    private String notifyMoneyRange;
    //签约日期
    private String signedDate;
    //签约渠道
    private String signedType;
    //签约机构
    private String signedAuthor;
    //缴费账户
    private AccountBean feeAccount;
    //月费
    private String feeRate;

    public SmsNotifyModel() {

    }

    public void convertToSmsNotifyModel(PsnSsmQueryResult.MaplistBean bean) {
        phoneNum = bean.getPushterm();
        minMoney = bean.getLoweramount() + "";
        maxMoney = bean.getUpperamount() + "";
        if (bean.getUpperamount() == 0d || bean.getUpperamount() == 0.00d) {
            maxMoney = "99999999999.99";
        }

        notifyMoneyRange = MoneyUtils.transMoneyFormat(bean.getLoweramount() + "", "001") + " - "
                + MoneyUtils.transMoneyFormat(bean.getUpperamount() + "", "001");
        if (bean.getSignchannel().equals("42")) {
            signedType = "电话银行";
            signedAuthor = "";
        } else if (bean.getSignchannel().equals("52")) {
            signedType = "自助终端";
            signedAuthor = bean.getSignprvno();
        } else if (bean.getSignchannel().equals("43")) {
            signedType = "批量签约";
            signedAuthor = bean.getSignprvno();
        } else if (bean.getSignchannel().equals("BL")) {
            signedType = "柜台签约";
            signedAuthor = bean.getSignprvno();
        } else if (bean.getSignchannel().equals("PE")) {
            signedType = "BPS批量签约";
            signedAuthor = bean.getSignprvno();
        } else if (bean.getSignchannel().equals("92")) {
            signedType = "POS";
            signedAuthor = bean.getSignprvno();
        } else if (bean.getSignchannel().equals("56")) {
            signedType = "电子渠道";
            signedAuthor = "";
        }
        signedDate = bean.getSigndate();
        feeRate = bean.getFeestandard();
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getNotifyMoneyRange() {
        return notifyMoneyRange;
    }

    public void setNotifyMoneyRange(String notifyMoneyRange) {
        this.notifyMoneyRange = notifyMoneyRange;
    }

    public AccountBean getSignAccount() {
        return signAccount;
    }

    public void setSignAccount(AccountBean signAccount) {
        this.signAccount = signAccount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(String signedDate) {
        this.signedDate = signedDate;
    }

    public String getSignedType() {
        return signedType;
    }

    public void setSignedType(String signedType) {
        this.signedType = signedType;
    }

    public String getSignedAuthor() {
        return signedAuthor;
    }

    public void setSignedAuthor(String signedAuthor) {
        this.signedAuthor = signedAuthor;
    }

    public AccountBean getFeeAccount() {
        return feeAccount;
    }

    public void setFeeAccount(AccountBean feeAccount) {
        this.feeAccount = feeAccount;
    }

    public void setFeeAccount(String feeAccount) {
        AccountBean bean = new AccountBean();
        bean.setAccountNumber(feeAccount);
        this.feeAccount = bean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.signAccount, flags);
        dest.writeString(this.userName);
        dest.writeString(this.phoneNum);
        dest.writeString(this.minMoney);
        dest.writeString(this.maxMoney);
        dest.writeString(this.notifyMoneyRange);
        dest.writeString(this.signedDate);
        dest.writeString(this.signedType);
        dest.writeString(this.signedAuthor);
        dest.writeParcelable(this.feeAccount, flags);
        dest.writeString(this.feeRate);
    }

    protected SmsNotifyModel(Parcel in) {
        this.signAccount = in.readParcelable(AccountBean.class.getClassLoader());
        this.userName = in.readString();
        this.phoneNum = in.readString();
        this.minMoney = in.readString();
        this.maxMoney = in.readString();
        this.notifyMoneyRange = in.readString();
        this.signedDate = in.readString();
        this.signedType = in.readString();
        this.signedAuthor = in.readString();
        this.feeAccount = in.readParcelable(AccountBean.class.getClassLoader());
        this.feeRate = in.readString();
    }

    public static final Parcelable.Creator<SmsNotifyModel> CREATOR = new Parcelable.Creator<SmsNotifyModel>() {
        @Override
        public SmsNotifyModel createFromParcel(Parcel source) {
            return new SmsNotifyModel(source);
        }

        @Override
        public SmsNotifyModel[] newArray(int size) {
            return new SmsNotifyModel[size];
        }
    };
}
