package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cry7096 on 2016/12/26.
 * 活动管理平台-转账汇款-跳微信上送字段model
 */
public class TransRemitCABIIEnterActyModel implements Parcelable {
    /**
     * 渠道  String {1：web，2：手机，4：homebank，5：微信，6：对接}
     */
    private String channel;
    /**
     * 票	String	票系统生成的票号
     */
    private String tokenCode;
    /**
     * 取票信息	String	customid|cif号|渠道|地区|
     */
    private String ticketMsg;
    /**
     * 活动id
     */
    private String actyId;

    public String getActyId() {
        return actyId;
    }

    public void setActyId(String actyId) {
        this.actyId = actyId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public String getTicketMsg() {
        return ticketMsg;
    }

    public void setTicketMsg(String ticketMsg) {
        this.ticketMsg = ticketMsg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channel);
        dest.writeString(this.tokenCode);
        dest.writeString(this.ticketMsg);
        dest.writeString(this.actyId);
    }

    public TransRemitCABIIEnterActyModel() {
    }

    protected TransRemitCABIIEnterActyModel(Parcel in) {
        this.channel = in.readString();
        this.tokenCode = in.readString();
        this.ticketMsg = in.readString();
        this.actyId = in.readString();
    }

    public static final Creator<TransRemitCABIIEnterActyModel> CREATOR = new Creator<TransRemitCABIIEnterActyModel>() {
        @Override
        public TransRemitCABIIEnterActyModel createFromParcel(Parcel source) {
            return new TransRemitCABIIEnterActyModel(source);
        }

        @Override
        public TransRemitCABIIEnterActyModel[] newArray(int size) {
            return new TransRemitCABIIEnterActyModel[size];
        }
    };
}
