package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;

/**
 * Name: liukai
 * Time：2016/12/9 9:12.
 * Created by lk7066 on 2016/12/9.
 * It's used to 交易流量设置数据
 */

public class AttCardTradeFlowModel implements Parcelable, BaseFillInfoBean {

    /**
     * 主卡accountId
     * */
    private String accountId;

    /**
     * 主卡卡号
     * */
    private String accountNo;

    /**
     * 附属卡姓名
     * */
    private String subCardName;

    /**
     * 什么字段
     * */
    private String CombinName;

    /**
     * 附属卡卡号*/
    private String subCrcdNo;

    /**
     * 币种
     * */
    private String currency;

    /**
     * 主卡类型
     * */
    private String masterCrcdType;

    /**
     * 流量金额
     * */
    private String amount;

    /**
     * 安全因子组合
     * */
    private String _combinId;

    /**
     * 会话
     * */
    private String conversationId;

    public static Creator<AttCardTradeFlowModel> getCREATOR() {
        return CREATOR;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSubCardName() {
        return subCardName;
    }

    public void setSubCardName(String subCardName) {
        this.subCardName = subCardName;
    }

    @Override
    public String getCombinName() {
        return CombinName;
    }

    @Override
    public void setCombinName(String combinName) {
        CombinName = combinName;
    }

    public String getSubCrcdNo() {
        return subCrcdNo;
    }

    public void setSubCrcdNo(String subCrcdNo) {
        this.subCrcdNo = subCrcdNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMasterCrcdType() {
        return masterCrcdType;
    }

    public void setMasterCrcdType(String masterCrcdType) {
        this.masterCrcdType = masterCrcdType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String get_combinId() {
        return _combinId;
    }

    @Override
    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.accountNo);
        dest.writeString(this.subCardName);
        dest.writeString(this.CombinName);
        dest.writeString(this.subCrcdNo);
        dest.writeString(this.currency);
        dest.writeString(this.masterCrcdType);
        dest.writeString(this.amount);
        dest.writeString(this._combinId);
    }

    public AttCardTradeFlowModel() {
    }

    protected AttCardTradeFlowModel(Parcel in) {
        this.accountId = in.readString();
        this.accountNo = in.readString();
        this.subCardName = in.readString();
        this.CombinName = in.readString();
        this.subCrcdNo = in.readString();
        this.currency = in.readString();
        this.masterCrcdType = in.readString();
        this.amount = in.readString();
        this._combinId = in.readString();
    }

    public static final Creator<AttCardTradeFlowModel> CREATOR = new Creator<AttCardTradeFlowModel>() {
        @Override
        public AttCardTradeFlowModel createFromParcel(Parcel source) {
            return new AttCardTradeFlowModel(source);
        }

        @Override
        public AttCardTradeFlowModel[] newArray(int size) {
            return new AttCardTradeFlowModel[size];
        }
    };

    @Override
    public String toString() {
        return "AttCardTradeFlowModel{" +
                "accountId='" + accountId + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", subCardName='" + subCardName + '\'' +
                ", CombinName='" + CombinName + '\'' +
                ", subCrcdNo='" + subCrcdNo + '\'' +
                ", currency='" + currency + '\'' +
                ", masterCrcdType='" + masterCrcdType + '\'' +
                ", amount='" + amount + '\'' +
                ", _combinId='" + _combinId + '\'' +
                '}';
    }

    @Override
    public String getConversationId() {
        return conversationId;
    }

    @Override
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
