package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupParams;

/**
 * Name: liukai
 * Time：2016/11/30 15:46.
 * Created by lk7066 on 2016/11/30.
 * It's used to
 */

public class AutoCrcdPayModel implements Parcelable {

    // 账户ID
    private String accountId;

    // 信用卡卡号
    private String accountNumber;

    // 本币账户ID（暂存ID）
    private String localAccountId;

    // 外币账户ID（暂存ID）
    private String foreignAccountId;

    //开通状态，0未开通，1已开通
    private int mCrcdStatus;

    //币种状态，0单外币，1单本币，2多币种
    private int mCurrencyStatus;

    //还款金额，1全额还款，2最低额还款
    private int mPaymentModeStatus;

    //还款方式，0人民币还款，1人民币和外币各自还款，2外币还款
    private int mPaymentWay;

    // 人民币还款账户ID
    private String rmbRepayAcctId;

    // 外币还款账户ID
    private String foreignRepayAcctId;

    //多币种的外币还款账户币种
    private String foreignCurrencyAccountCurrency;

    // 本币还款账号
    private String localCurrencyPaymentAccountNo;

    // 外币还款账号
    private String foreignCurrencyAccountNo;

    // 还款方式，0=主动还款，1=自动还款
    private String repayType;

    // 自动还款金额设定，FULL=全额还款，MIMP=最低额还款，主动还款上送-1
    private String autoRepayMode;

    // 主动还款类型，0=所有结欠均以人民币还款，1=人民币与外币结欠分别以相应账户还款，2=外币结欠以相应账户还款，主动还款时上送-1
    private String repayCurSel;

    // 所有结欠以人民币还款时还款账户ID，repayCurSel=0时上送
    private String repayAcctId;

    // 单外币还款账户ID，repayCurSel=2时上送
    private String signForeignCurrencyAcctId;

    // TokenID
    private String token;

    // 会话ID
    private String conversationId;

    // 设备指纹
    private String devicePrint;

    public String getLocalAccountId() {
        return localAccountId;
    }

    public void setLocalAccountId(String localAccountId) {
        this.localAccountId = localAccountId;
    }

    public String getForeignAccountId() {
        return foreignAccountId;
    }

    public void setForeignAccountId(String foreignAccountId) {
        this.foreignAccountId = foreignAccountId;
    }

    public String getForeignCurrencyAccountCurrency() {
        return foreignCurrencyAccountCurrency;
    }

    public void setForeignCurrencyAccountCurrency(String foreignCurrencyAccountCurrency) {
        this.foreignCurrencyAccountCurrency = foreignCurrencyAccountCurrency;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRepayAcctId() {
        return repayAcctId;
    }

    public void setRepayAcctId(String repayAcctId) {
        this.repayAcctId = repayAcctId;
    }

    public String getRepayCurSel() {
        return repayCurSel;
    }

    public void setRepayCurSel(String repayCurSel) {
        this.repayCurSel = repayCurSel;
    }

    public String getAutoRepayMode() {
        return autoRepayMode;
    }

    public void setAutoRepayMode(String autoRepayMode) {
        this.autoRepayMode = autoRepayMode;
    }

    public String getLocalCurrencyPaymentAccountNo() {
        return localCurrencyPaymentAccountNo;
    }

    public void setLocalCurrencyPaymentAccountNo(String localCurrencyPaymentAccountNo) {
        this.localCurrencyPaymentAccountNo = localCurrencyPaymentAccountNo;
    }

    public String getForeignCurrencyAccountNo() {
        return foreignCurrencyAccountNo;
    }

    public void setForeignCurrencyAccountNo(String foreignCurrencyAccountNo) {
        this.foreignCurrencyAccountNo = foreignCurrencyAccountNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRmbRepayAcctId() {
        return rmbRepayAcctId;
    }

    public void setRmbRepayAcctId(String rmbRepayAcctId) {
        this.rmbRepayAcctId = rmbRepayAcctId;
    }

    public String getForeignRepayAcctId() {
        return foreignRepayAcctId;
    }

    public void setForeignRepayAcctId(String foreignRepayAcctId) {
        this.foreignRepayAcctId = foreignRepayAcctId;
    }

    public String getSignForeignCurrencyAcctId() {
        return signForeignCurrencyAcctId;
    }

    public void setSignForeignCurrencyAcctId(String signForeignCurrencyAcctId) {
        this.signForeignCurrencyAcctId = signForeignCurrencyAcctId;
    }

    public int getmCrcdStatus() {
        return mCrcdStatus;
    }

    public void setmCrcdStatus(int mCrcdStatus) {
        this.mCrcdStatus = mCrcdStatus;
    }

    public int getmCurrencyStatus() {
        return mCurrencyStatus;
    }

    public void setmCurrencyStatus(int mCurrencyStatus) {
        this.mCurrencyStatus = mCurrencyStatus;
    }

    public int getmPaymentModeStatus() {
        return mPaymentModeStatus;
    }

    public void setmPaymentModeStatus(int mPaymentModeStatus) {
        this.mPaymentModeStatus = mPaymentModeStatus;
    }

    public int getmPaymentWay() {
        return mPaymentWay;
    }

    public void setmPaymentWay(int mPaymentWay) {
        this.mPaymentWay = mPaymentWay;
    }

    public AutoCrcdPayModel() {
    }

    @Override
    public String toString() {
        return "---liukai---" + "AutoCrcdPayModel{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", localAccountId='" + localAccountId + '\'' +
                ", foreignAccountId='" + foreignAccountId + '\'' +
                ", mCrcdStatus=" + mCrcdStatus +
                ", mCurrencyStatus=" + mCurrencyStatus +
                ", mPaymentModeStatus=" + mPaymentModeStatus +
                ", mPaymentWay=" + mPaymentWay +
                ", rmbRepayAcctId='" + rmbRepayAcctId + '\'' +
                ", foreignRepayAcctId='" + foreignRepayAcctId + '\'' +
                ", foreignCurrencyAccountCurrency='" + foreignCurrencyAccountCurrency + '\'' +
                ", localCurrencyPaymentAccountNo='" + localCurrencyPaymentAccountNo + '\'' +
                ", foreignCurrencyAccountNo='" + foreignCurrencyAccountNo + '\'' +
                ", repayType='" + repayType + '\'' +
                ", autoRepayMode='" + autoRepayMode + '\'' +
                ", repayCurSel='" + repayCurSel + '\'' +
                ", repayAcctId='" + repayAcctId + '\'' +
                ", signForeignCurrencyAcctId='" + signForeignCurrencyAcctId + '\'' +
                ", token='" + token + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", devicePrint='" + devicePrint + '\'' +
                '}';
    }

    public PsnCrcdPaymentWaySetupParams setPaymentWayParams(){
        PsnCrcdPaymentWaySetupParams params = new PsnCrcdPaymentWaySetupParams();

        /**
         * accountId : 208367999
         * repayType : 1
         * autoRepayMode : FULL
         * repayCurSel : 0
         * rmbRepayAcctId :
         * foreignRepayAcctId :
         * repayAcctId : 208367002
         * signForeignCurrencyAcctId :
         * token : cecvnuw6
         * conversationId : 6d48fe12-e34a-4089-926d-294ff6ee76ac
         */

        params.setAccountId(this.accountId);
        params.setRepayType(this.repayType);
        params.setAutoRepayMode(this.autoRepayMode);
        params.setRepayCurSel(this.repayCurSel);
        params.setRmbRepayAcctId(this.rmbRepayAcctId);
        params.setForeignRepayAcctId(this.foreignRepayAcctId);
        params.setRepayAcctId(this.repayAcctId);
        params.setSignForeignCurrencyAcctId(this.signForeignCurrencyAcctId);
        params.setToken(this.token);
        params.setDevicePrint(this.devicePrint);
        params.setConversationId(this.conversationId);
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.accountNumber);
        dest.writeString(this.localAccountId);
        dest.writeString(this.foreignAccountId);
        dest.writeInt(this.mCrcdStatus);
        dest.writeInt(this.mCurrencyStatus);
        dest.writeInt(this.mPaymentModeStatus);
        dest.writeInt(this.mPaymentWay);
        dest.writeString(this.rmbRepayAcctId);
        dest.writeString(this.foreignRepayAcctId);
        dest.writeString(this.foreignCurrencyAccountCurrency);
        dest.writeString(this.localCurrencyPaymentAccountNo);
        dest.writeString(this.foreignCurrencyAccountNo);
        dest.writeString(this.repayType);
        dest.writeString(this.autoRepayMode);
        dest.writeString(this.repayCurSel);
        dest.writeString(this.repayAcctId);
        dest.writeString(this.signForeignCurrencyAcctId);
        dest.writeString(this.token);
        dest.writeString(this.conversationId);
        dest.writeString(this.devicePrint);
    }

    protected AutoCrcdPayModel(Parcel in) {
        this.accountId = in.readString();
        this.accountNumber = in.readString();
        this.localAccountId = in.readString();
        this.foreignAccountId = in.readString();
        this.mCrcdStatus = in.readInt();
        this.mCurrencyStatus = in.readInt();
        this.mPaymentModeStatus = in.readInt();
        this.mPaymentWay = in.readInt();
        this.rmbRepayAcctId = in.readString();
        this.foreignRepayAcctId = in.readString();
        this.foreignCurrencyAccountCurrency = in.readString();
        this.localCurrencyPaymentAccountNo = in.readString();
        this.foreignCurrencyAccountNo = in.readString();
        this.repayType = in.readString();
        this.autoRepayMode = in.readString();
        this.repayCurSel = in.readString();
        this.repayAcctId = in.readString();
        this.signForeignCurrencyAcctId = in.readString();
        this.token = in.readString();
        this.conversationId = in.readString();
        this.devicePrint = in.readString();
    }

    public static final Creator<AutoCrcdPayModel> CREATOR = new Creator<AutoCrcdPayModel>() {
        @Override
        public AutoCrcdPayModel createFromParcel(Parcel source) {
            return new AutoCrcdPayModel(source);
        }

        @Override
        public AutoCrcdPayModel[] newArray(int size) {
            return new AutoCrcdPayModel[size];
        }
    };
}
