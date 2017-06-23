package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import android.os.Parcel;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;

/**
 * Contact：双向宝-新增保证金账户-双向宝签约预交易
 * Created by zhx on 2016/12/12
 */
public class VFGSignPreViewModel implements BaseFillInfoBean, android.os.Parcelable {
    // 账户标识
    private String accountId;
    // 借记卡卡号
    private String accountNumber;
    // 保证金产品名称
    private String bailName;
    // 保证金产品序号
    private String bailNo;
    private String settleCurrency;
    // 安全因子组合id(必输(服务码PB081))
    private String _combinId;
    private String combinName;
    private String conversationId;

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

    public String getBailName() {
        return bailName;
    }

    public void setBailName(String bailName) {
        this.bailName = bailName;
    }

    public String getBailNo() {
        return bailNo;
    }

    public void setBailNo(String bailNo) {
        this.bailNo = bailNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
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
    public String getCombinName() {
        return combinName;
    }

    @Override
    public void setCombinName(String combinName) {
        this.combinName = combinName;
    }

    @Override
    public String getConversationId() {
        return conversationId;
    }

    @Override
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.accountNumber);
        dest.writeString(this.bailName);
        dest.writeString(this.bailNo);
        dest.writeString(this.settleCurrency);
        dest.writeString(this._combinId);
        dest.writeString(this.combinName);
        dest.writeString(this.conversationId);
    }

    public VFGSignPreViewModel() {
    }

    private VFGSignPreViewModel(Parcel in) {
        this.accountId = in.readString();
        this.accountNumber = in.readString();
        this.bailName = in.readString();
        this.bailNo = in.readString();
        this.settleCurrency = in.readString();
        this._combinId = in.readString();
        this.combinName = in.readString();
        this.conversationId = in.readString();
    }

    public static final Creator<VFGSignPreViewModel> CREATOR = new Creator<VFGSignPreViewModel>() {
        public VFGSignPreViewModel createFromParcel(Parcel source) {
            return new VFGSignPreViewModel(source);
        }

        public VFGSignPreViewModel[] newArray(int size) {
            return new VFGSignPreViewModel[size];
        }
    };
}