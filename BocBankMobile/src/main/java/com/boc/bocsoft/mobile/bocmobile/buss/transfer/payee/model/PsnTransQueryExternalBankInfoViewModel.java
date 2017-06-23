package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 转账银行开户行查询
 * Created by zhx on 2016/7/21
 */
public class PsnTransQueryExternalBankInfoViewModel {
    private String toBankCode;
    private String bankName;
    private Integer currentIndex;
    private Integer pageSize;

    public String getToBankCode() {
        return toBankCode;
    }

    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private Integer recordnumber;

    private List<OpenBankBean> list = new ArrayList<OpenBankBean>();

    public Integer getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(Integer recordnumber) {
        this.recordnumber = recordnumber;
    }

    public List<OpenBankBean> getList() {
        return list;
    }

    public void setList(List<OpenBankBean> list) {
        this.list = list;
    }

    public static class OpenBankBean implements Parcelable {
        private String bankName;
        private String cnapsCode;

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.bankName);
            dest.writeString(this.cnapsCode);
        }

        public OpenBankBean() {
        }

        private OpenBankBean(Parcel in) {
            this.bankName = in.readString();
            this.cnapsCode = in.readString();
        }

        public static final Parcelable.Creator<OpenBankBean> CREATOR = new Parcelable.Creator<OpenBankBean>() {
            public OpenBankBean createFromParcel(Parcel source) {
                return new OpenBankBean(source);
            }

            public OpenBankBean[] newArray(int size) {
                return new OpenBankBean[size];
            }
        };
    }
}
