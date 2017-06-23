package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Contact：中银理财-交易查询页面
 * Created by zhx on 2016/9/7
 */
public class XpadAccountQueryViewModel {
    // 查询类型(0：查询所有已登记的理财账户 1、查询所有已登记并且关联到网银的理财账户)
    private String queryType;
    // 账户状态(0：停用 1：可用 不输代表查询全部)
    private String xpadAccountSatus;

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public void setXpadAccountSatus(String xpadAccountSatus) {
        this.xpadAccountSatus = xpadAccountSatus;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getXpadAccountSatus() {
        return xpadAccountSatus;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    private List<XPadAccountEntity> list;

    public void setList(List<XPadAccountEntity> list) {
        this.list = list;
    }

    public List<XPadAccountEntity> getList() {
        return list;
    }

    public static class XPadAccountEntity implements Parcelable {
        /**
         * bancID : 03206
         * accountId : 404421447
         * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
         * accountType : 119
         * xpadAccount : 100220095288
         * ibkNumber : 40740
         * xpadAccountSatus : 1
         * accountNo : 100220095288
         */

        // 客户理财账户（加星）
        private String xpadAccount;
        // 资金账户（加星）
        private String accountNo;
        // 账户类型
        private String accountType;
        // 账户开户行(核心系统中的账号开户行)
        private String bancID;
        // 账户状态(0：停用 1：可用)
        private String xpadAccountSatus;
        // 账户ID(已关联进网银的账户返回，未关联进网银的为空)
        private int accountId;
        // 省行联行号
        private String ibkNumber;
        // 账户缓存标识(所有账户均返回，客户非敏感数据)
        private String accountKey;

        public void setBancID(String bancID) {
            this.bancID = bancID;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public void setXpadAccount(String xpadAccount) {
            this.xpadAccount = xpadAccount;
        }

        public void setIbkNumber(String ibkNumber) {
            this.ibkNumber = ibkNumber;
        }

        public void setXpadAccountSatus(String xpadAccountSatus) {
            this.xpadAccountSatus = xpadAccountSatus;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getBancID() {
            return bancID;
        }

        public int getAccountId() {
            return accountId;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public String getAccountType() {
            return accountType;
        }

        public String getXpadAccount() {
            return xpadAccount;
        }

        public String getIbkNumber() {
            return ibkNumber;
        }

        public String getXpadAccountSatus() {
            return xpadAccountSatus;
        }

        public String getAccountNo() {
            return accountNo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.xpadAccount);
            dest.writeString(this.accountNo);
            dest.writeString(this.accountType);
            dest.writeString(this.bancID);
            dest.writeString(this.xpadAccountSatus);
            dest.writeInt(this.accountId);
            dest.writeString(this.ibkNumber);
            dest.writeString(this.accountKey);
        }

        public XPadAccountEntity() {
        }

        private XPadAccountEntity(Parcel in) {
            this.xpadAccount = in.readString();
            this.accountNo = in.readString();
            this.accountType = in.readString();
            this.bancID = in.readString();
            this.xpadAccountSatus = in.readString();
            this.accountId = in.readInt();
            this.ibkNumber = in.readString();
            this.accountKey = in.readString();
        }

        public static final Parcelable.Creator<XPadAccountEntity> CREATOR = new Parcelable.Creator<XPadAccountEntity>() {
            public XPadAccountEntity createFromParcel(Parcel source) {
                return new XPadAccountEntity(source);
            }

            public XPadAccountEntity[] newArray(int size) {
                return new XPadAccountEntity[size];
            }
        };
    }
}