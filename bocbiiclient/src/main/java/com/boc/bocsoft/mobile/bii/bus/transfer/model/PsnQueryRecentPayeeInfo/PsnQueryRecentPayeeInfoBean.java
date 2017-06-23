package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo;

import java.util.List;

/**
 * Created by WYme on 2016/8/29.
 */
public class PsnQueryRecentPayeeInfoBean {

        private String     payeetId;
        private String    accountName;
        private String    accountNumber;
        private String     type;
        private String    accountIbkNum;
        private String     mobile;
        private String    bankName;
        private String       address;
        private String     cnapsCode;
        private String        bocFlag;

        public String getPayeetId() {
            return payeetId;
        }

        public void setPayeetId(String payeetId) {
            this.payeetId = payeetId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }

        public String getBocFlag() {
            return bocFlag;
        }

        public void setBocFlag(String bocFlag) {
            this.bocFlag = bocFlag;
        }

        public String getIsAppointed() {
            return isAppointed;
        }

        public void setIsAppointed(String isAppointed) {
            this.isAppointed = isAppointed;
        }

        private String isAppointed;
    }
