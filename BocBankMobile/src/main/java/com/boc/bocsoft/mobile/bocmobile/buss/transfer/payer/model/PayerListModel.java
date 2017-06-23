package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by liuyang on 2016/7/21.
 */
public class PayerListModel {
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "QueryPayerListViewModel{" +
                "result=" + result +
                '}';
    }

    public static class ResultBean implements Parcelable {
        private String payerName;
        private String payerCustomerId;
        private String payerMobile;
        private String identifyType;
        private String payerId;


        public ResultBean(Parcel in) {
            payerName = in.readString();
            payerCustomerId = in.readString();
            payerMobile = in.readString();
            identifyType = in.readString();
            payerId = in.readString();

        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

        public ResultBean() {

        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public String getPayerCustomerId() {
            return payerCustomerId;
        }

        public void setPayerCustomerId(String payerCustomerId) {
            this.payerCustomerId = payerCustomerId;
        }

        public String getPayerMobile() {
            return payerMobile;
        }

        public void setPayerMobile(String payerMobile) {
            this.payerMobile = payerMobile;
        }

        public String getIdentifyType() {
            return identifyType;
        }

        public void setIdentifyType(String identifyType) {
            this.identifyType = identifyType;
        }

        public String getPayerId() {
            return payerId;
        }

        public void setPayerId(String payerId) {
            this.payerId = payerId;
        }


        @Override
        public String toString() {
            return "ResultBean{" +
                    "payerName='" + payerName + '\'' +
                    ", payerCustomerId=" + payerCustomerId +
                    ", payerMobile='" + payerMobile + '\'' +
                    ", identifyType='" + identifyType + '\'' +
                    ", payerId=" + payerId +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(payerName);
            dest.writeString(payerCustomerId);
            dest.writeString(payerMobile);
            dest.writeString(identifyType);
            dest.writeString(payerId);
        }
    }


    /**
     * 付款人ID
     */
    private String payerId;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    private String payerMobile;

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
