package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * ViewModel：查询付款人列表
 * Created by zhx on 2016/6/30.
 */
public class QueryPayerListViewModel {


    private ArrayList<ResultBean> result;


    public ArrayList<ResultBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "QueryPayerListViewModel{" +
                "result=" + result +
                '}';
    }

    public static class ResultBean implements Parcelable {
        /**
         * 付款人姓名
         */
        private String payerName;
        /**
         * 付款人客户号
         */
        private String payerCustomerId;
        /**
         * 付款人手机
         */
        private String payerMobile;
        /**
         * 付款人类型
         */
        private String identifyType;
        /**
         * 付款人ID
         */
        private String payerId;
        /**
         * 付款人是否选中的标记
         */
        private boolean isChecked;

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

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
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
            dest.writeString(this.payerName);
            dest.writeString(this.payerCustomerId);
            dest.writeString(this.payerMobile);
            dest.writeString(this.identifyType);
            dest.writeString(this.payerId);
            dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
        }

        public ResultBean() {
        }

        private ResultBean(Parcel in) {
            this.payerName = in.readString();
            this.payerCustomerId = in.readString();
            this.payerMobile = in.readString();
            this.identifyType = in.readString();
            this.payerId = in.readString();
            this.isChecked = in.readByte() != 0;
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
