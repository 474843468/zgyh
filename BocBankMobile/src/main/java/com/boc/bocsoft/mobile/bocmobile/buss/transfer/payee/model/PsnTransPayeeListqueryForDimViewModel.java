package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询收款人列表数据请求
 * Created by zhx on 2016/8/31
 */
public class PsnTransPayeeListqueryForDimViewModel {
    /**
     * 是否定向收款人
     */
    private String isAppointed;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 当前页
     */
    private String currentIndex;
    /**
     * 每页显示条数
     */
    private String pageSize;
    /**
     * 收款账户标志
     */
    private String[] bocFlag;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getIsAppointed() {
        return isAppointed;
    }

    public void setIsAppointed(String isAppointed) {
        this.isAppointed = isAppointed;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String[] bocFlag) {
        this.bocFlag = bocFlag;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private int recordCount;

    private List<PayeeEntity> payeeEntityList = new ArrayList<PayeeEntity>();

    public List<PayeeEntity> getPayeeEntityList() {
        return payeeEntityList;
    }

    public void setPayeeEntityList(List<PayeeEntity> payeeEntityList) {
        this.payeeEntityList = payeeEntityList;
    }

    public static class PayeeEntity implements Comparable<PayeeEntity>, Parcelable {
        //地址
        private String address;
        //帐户类型
        private String type;
        //手机号
        private String mobile;
        //账户
        private String accountNumber;
        // 是否定向收款人
        private String isAppointed;
        //收款人中文名称(跨境汇款)
        private String payeeCNName;
        //收款账户标志
        private String bocFlag;
        //收款行行号(跨境汇款)
        private String bankNum;
        //收款行联行号（所属地区）
        private String accountIbkNum;
        //收款行所属银行
        private String bankCode;
        //CNAPS号
        private String cnapsCode;
        //收款人别名
        private String payeeAlias;
        //收款人帐户名称
        private String accountName;
        //收款人银行名称
        private String bankName;
        //二代支付行行名
        private String payBankName;
        //二代支付行行号
        private String payBankCode;
        //收款人常驻国家(跨境汇款)
        private String countryCode;
        //收款人邮政编码
        private String postcode;
        //收款地区(跨境汇款)
        private String regionCode;
        //收款银行SWIFT CODE
        private String swift;
        // 收款人ID
        private Integer payeetId;

        private boolean isLinked = false; //是否定向收款人
        private String accountId;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        private String nickName;//账户别名，2016-9-23 wangy添加

        public String getNickNamepinyin() {
            return nickNamepinyin;
        }

        public void setNickNamepinyin(String nickNamepinyin) {
            if (!TextUtils.isEmpty(nickNamepinyin)) {
                this.nickNamepinyin = nickNamepinyin;
            } else {
                String nickNamePinYin = PinYinUtil.getPinYin(this.nickName);
                nickNamePinYin = nickNamePinYin.trim();
                this.nickNamepinyin = TextUtils.isEmpty(nickNamePinYin) ? "z" : nickNamePinYin.toUpperCase();
            }

        }

        private String nickNamepinyin;//由 nickname生成
        /**
         * 拼音，用来排序的（由accountName字段生成）
         */
        private String pinyin;

        @Override
        public int compareTo(PayeeEntity another) {
            return this.getPinyin().compareTo(another.getPinyin());
        }

        public PayeeEntity() {
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getIsAppointed() {
            return isAppointed;
        }

        public void setIsAppointed(String isAppointed) {
            this.isAppointed = isAppointed;
        }

        public String getPayeeCNName() {
            return payeeCNName;
        }

        public void setPayeeCNName(String payeeCNName) {
            this.payeeCNName = payeeCNName;
        }

        public String getBocFlag() {
            return bocFlag;
        }

        public void setBocFlag(String bocFlag) {
            this.bocFlag = bocFlag;
        }

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }

        public String getPayeeAlias() {
            return payeeAlias;
        }

        public void setPayeeAlias(String payeeAlias) {
            this.payeeAlias = payeeAlias;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getPayBankName() {
            return payBankName;
        }

        public void setPayBankName(String payBankName) {
            this.payBankName = payBankName;
        }

        public String getPayBankCode() {
            return payBankCode;
        }

        public void setPayBankCode(String payBankCode) {
            this.payBankCode = payBankCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getSwift() {
            return swift;
        }

        public void setSwift(String swift) {
            this.swift = swift;
        }

        public Integer getPayeetId() {
            return payeetId;
        }

        public void setPayeetId(Integer payeetId) {
            this.payeetId = payeetId;
        }

        public boolean isLinked() {
            return isLinked;
        }

        public void setLinked(boolean linked) {
            isLinked = linked;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            if (!TextUtils.isEmpty(pinyin)) {
                this.pinyin = pinyin;
            } else {
                String pinYin = PinYinUtil.getPinYin(this.accountName);
                pinYin = pinYin.trim();
                this.pinyin = TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase();
            }
        }

        public static Creator<PayeeEntity> getCREATOR() {
            return CREATOR;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.address);
            dest.writeString(this.type);
            dest.writeString(this.mobile);
            dest.writeString(this.accountNumber);
            dest.writeString(this.isAppointed);
            dest.writeString(this.payeeCNName);
            dest.writeString(this.bocFlag);
            dest.writeString(this.bankNum);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.bankCode);
            dest.writeString(this.cnapsCode);
            dest.writeString(this.payeeAlias);
            dest.writeString(this.accountName);
            dest.writeString(this.bankName);
            dest.writeString(this.payBankName);
            dest.writeString(this.payBankCode);
            dest.writeString(this.countryCode);
            dest.writeString(this.postcode);
            dest.writeString(this.regionCode);
            dest.writeString(this.swift);
            dest.writeValue(this.payeetId);
            dest.writeByte(this.isLinked ? (byte) 1 : (byte) 0);
            dest.writeString(this.accountId);
            dest.writeString(this.nickName);
            dest.writeString(this.nickNamepinyin);
            dest.writeString(this.pinyin);
        }

        protected PayeeEntity(Parcel in) {
            this.address = in.readString();
            this.type = in.readString();
            this.mobile = in.readString();
            this.accountNumber = in.readString();
            this.isAppointed = in.readString();
            this.payeeCNName = in.readString();
            this.bocFlag = in.readString();
            this.bankNum = in.readString();
            this.accountIbkNum = in.readString();
            this.bankCode = in.readString();
            this.cnapsCode = in.readString();
            this.payeeAlias = in.readString();
            this.accountName = in.readString();
            this.bankName = in.readString();
            this.payBankName = in.readString();
            this.payBankCode = in.readString();
            this.countryCode = in.readString();
            this.postcode = in.readString();
            this.regionCode = in.readString();
            this.swift = in.readString();
            this.payeetId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.isLinked = in.readByte() != 0;
            this.accountId = in.readString();
            this.nickName = in.readString();
            this.nickNamepinyin = in.readString();
            this.pinyin = in.readString();
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
