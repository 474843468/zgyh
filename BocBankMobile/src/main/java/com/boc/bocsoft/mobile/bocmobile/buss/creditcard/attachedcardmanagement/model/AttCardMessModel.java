package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultParams;

/**
 * Name: liukai
 * Time：2016/12/9 9:11.
 * Created by lk7066 on 2016/12/9.
 * It's used to 交易短信设置的数据
 */

public class AttCardMessModel implements Parcelable{

    /**
     * 信用卡Id
     * */
    private String accountId;

    /**
     * 附属卡卡号
     * */
    private String subCrcdNo;

    /**
     * 短信标识
     * */
    private String smeSendFlag;

    /**
     * 信用卡卡号
     * */
    private String crcdNo;

    /**
     * 附属卡户主
     * */
    private String attCardName;

    /**
     * 会话
     * */
    private String conversationId;

    /**
     * token
     * */
    private String Token;

    @Override
    public String toString() {
        return "AttCardMessModel{" +
                "accountId='" + accountId + '\'' +
                ", subCrcdNo='" + subCrcdNo + '\'' +
                ", smeSendFlag='" + smeSendFlag + '\'' +
                ", crcdNo='" + crcdNo + '\'' +
                ", attCardName='" + attCardName + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", Token='" + Token + '\'' +
                '}';
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCrcdNo() {
        return crcdNo;
    }

    public void setCrcdNo(String crcdNo) {
        this.crcdNo = crcdNo;
    }

    public String getAttCardName() {
        return attCardName;
    }

    public void setAttCardName(String attCardName) {
        this.attCardName = attCardName;
    }

    public static Creator<AttCardMessModel> getCREATOR() {
        return CREATOR;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSubCrcdNo() {
        return subCrcdNo;
    }

    public void setSubCrcdNo(String subCrcdNo) {
        this.subCrcdNo = subCrcdNo;
    }

    public String getSmeSendFlag() {
        return smeSendFlag;
    }

    public void setSmeSendFlag(String smeSendFlag) {
        this.smeSendFlag = smeSendFlag;
    }


    public AttCardMessModel() {
    }

    public PsnCrcdAppertainMessSetResultParams setMessResultParams(){
        PsnCrcdAppertainMessSetResultParams params = new PsnCrcdAppertainMessSetResultParams();
        params.setAccountId(this.accountId);
        params.setCardNo(this.subCrcdNo);
        params.setSendMessMode(this.smeSendFlag);
        params.setConversationId(this.conversationId);
        params.setToken(this.Token);
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.subCrcdNo);
        dest.writeString(this.smeSendFlag);
        dest.writeString(this.crcdNo);
        dest.writeString(this.attCardName);
        dest.writeString(this.conversationId);
        dest.writeString(this.Token);
    }

    protected AttCardMessModel(Parcel in) {
        this.accountId = in.readString();
        this.subCrcdNo = in.readString();
        this.smeSendFlag = in.readString();
        this.crcdNo = in.readString();
        this.attCardName = in.readString();
        this.conversationId = in.readString();
        this.Token = in.readString();
    }

    public static final Creator<AttCardMessModel> CREATOR = new Creator<AttCardMessModel>() {
        @Override
        public AttCardMessModel createFromParcel(Parcel source) {
            return new AttCardMessModel(source);
        }

        @Override
        public AttCardMessModel[] newArray(int size) {
            return new AttCardMessModel[size];
        }
    };

}
