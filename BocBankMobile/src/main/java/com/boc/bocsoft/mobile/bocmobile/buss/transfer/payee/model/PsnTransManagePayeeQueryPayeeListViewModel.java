package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询收款人列表（PsnTransPayeeListqueryForDim）
 * Created by zhx on 2016/7/25
 */
public class PsnTransManagePayeeQueryPayeeListViewModel {
    private String bocFlag; // bocFlag，因为此处搞错，导致半天没有弄好（F和f，大小写搞错了）

    private List<PayeeEntity> mPayeeEntityList = new ArrayList<PayeeEntity>();

    public String getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String bocFlag) {
        this.bocFlag = bocFlag;
    }

    public List<PayeeEntity> getPayeeEntityList() {
        return mPayeeEntityList;
    }

    public void setPayeeEntityList(List<PayeeEntity> payeeEntityList) {
        mPayeeEntityList = payeeEntityList;
    }

    public static class PayeeEntity implements Comparable<PayeeEntity>, Parcelable {
        /**
         * 拼音，用来排序的（由accountName字段生成）
         */
        private String pinyin;
        /**
         * 地址(收款人为国内跨行时该字段表示分行行名,收款人为跨境时该字段表示分行地址“收款人地址”)
         */
        private String address;
        /**
         * 帐户类型
         */
        private String type;
        /**
         * 账户
         */
        private String accountNumber;
        /**
         * 收款人中文名称(跨境汇款)
         */
        private String payeeCNName;
        /**
         * 收款人银行名称
         */
        private String bankName;
        /**
         * 收款人别名
         */
        private String payeeAlias;
        /**
         * 收款人别名
         */
        private String mobile;
        /**
         * 收款行所属银行
         */
        private String bankCode;
        /**
         * 收款行所属银行
         */
        private String regionCode;
        /**
         * 收款人邮政编码
         */
        private String postcode;
        /**
         * 收款行行号(跨境汇款)
         */
        private String bankNum;
        /**
         * 收款人常驻国家(跨境汇款)
         */
        private String countryCode;
        /**
         * 二代支付行行号
         */
        private String payBankCode;
        /**
         * 二代支付行行名
         */
        private String payBankName;
        /**
         * 收款人帐户名称
         */
        private String accountName;
        /**
         * 收款行联行号（所属地区）
         */
        private String accountIbkNum;
        /**
         * 收款银行SWIFT CODE
         */
        private String swift;
        /**
         * 收款人ID
         */
        private String payeetId;
        /**
         * 收款账户标志(0他行,1中行,2信用卡(对私),3二代支付,4跨境汇款收款人)
         */
        private String bocFlag;
        /**
         * CNAPS号
         */
        private String cnapsCode;
        /**
         * 收款人账户类型
         */
        private String accountType;

        private boolean isDingxiang;




        /**
         * wangyuan 2016-8-4
         *
         */
        private boolean isLinked=false;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        private String accountId;
        public boolean isLinked() {
            return isLinked;
        }

        public void setLinked(boolean linked) {
            isLinked = linked;
        }






        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getPayeeCNName() {
            return payeeCNName;
        }

        public void setPayeeCNName(String payeeCNName) {
            this.payeeCNName = payeeCNName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getPayeeAlias() {
            return payeeAlias;
        }

        public void setPayeeAlias(String payeeAlias) {
            this.payeeAlias = payeeAlias;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }


        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPayBankCode() {
            return payBankCode;
        }

        public void setPayBankCode(String payBankCode) {
            this.payBankCode = payBankCode;
        }

        public String getPayBankName() {
            return payBankName;
        }

        public void setPayBankName(String payBankName) {
            this.payBankName = payBankName;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getSwift() {
            return swift;
        }

        public void setSwift(String swift) {
            this.swift = swift;
        }

        public String getPayeetId() {
            return payeetId;
        }

        public void setPayeetId(String payeetId) {
            this.payeetId = payeetId;
        }

        public String getBocFlag() {
            return bocFlag;
        }

        public void setBocFlag(String bocFlag) {
            this.bocFlag = bocFlag;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }

        public String getPinyin() {
            return pinyin;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        /**
         * 此方法一般传入“”或者null即可
         *
         * @param pinyin
         */
        public void setPinyin(String pinyin) {
            if (!TextUtils.isEmpty(pinyin)) {
                this.pinyin = pinyin;
            } else {
                String pinYin = PinYinUtil.getPinYin(this.accountName);
                pinYin = pinYin.trim();
                this.pinyin = TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase();
            }
        }

        public boolean isDingxiang() {
            return isDingxiang;
        }

        public void setDingxiang(boolean dingxiang) {
            isDingxiang = dingxiang;
        }

        @Override
        public int compareTo(PayeeEntity another) {
            return this.getPinyin().compareTo(another.getPinyin());
        }

        public PayeeEntity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.pinyin);
            dest.writeString(this.address);
            dest.writeString(this.type);
            dest.writeString(this.accountNumber);
            dest.writeString(this.payeeCNName);
            dest.writeString(this.bankName);
            dest.writeString(this.payeeAlias);
            dest.writeString(this.mobile);
            dest.writeString(this.bankCode);
            dest.writeString(this.regionCode);
            dest.writeString(this.postcode);
            dest.writeString(this.bankNum);
            dest.writeString(this.countryCode);
            dest.writeString(this.payBankCode);
            dest.writeString(this.payBankName);
            dest.writeString(this.accountName);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.swift);
            dest.writeString(this.payeetId);
            dest.writeString(this.bocFlag);
            dest.writeString(this.cnapsCode);
            dest.writeString(this.accountType);
            dest.writeByte(this.isDingxiang ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isLinked ? (byte) 1 : (byte) 0);
            dest.writeString(this.accountId);
        }

        protected PayeeEntity(Parcel in) {
            this.pinyin = in.readString();
            this.address = in.readString();
            this.type = in.readString();
            this.accountNumber = in.readString();
            this.payeeCNName = in.readString();
            this.bankName = in.readString();
            this.payeeAlias = in.readString();
            this.mobile = in.readString();
            this.bankCode = in.readString();
            this.regionCode = in.readString();
            this.postcode = in.readString();
            this.bankNum = in.readString();
            this.countryCode = in.readString();
            this.payBankCode = in.readString();
            this.payBankName = in.readString();
            this.accountName = in.readString();
            this.accountIbkNum = in.readString();
            this.swift = in.readString();
            this.payeetId = in.readString();
            this.bocFlag = in.readString();
            this.cnapsCode = in.readString();
            this.accountType = in.readString();
            this.isDingxiang = in.readByte() != 0;
            this.isLinked = in.readByte() != 0;
            this.accountId = in.readString();
        }

        public static final Creator<PayeeEntity> CREATOR = new Creator<PayeeEntity>() {
            @Override
            public PayeeEntity createFromParcel(Parcel source) {
                return new PayeeEntity(source);
            }

            @Override
            public PayeeEntity[] newArray(int size) {
                return new PayeeEntity[size];
            }
        };
    }
}
