package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel:查询定向收款人列表
 * Created by zhx on 2016/7/20
 */
public class PsnDirTransQueryPayeeListViewModel {
    /**
     * 收款账户中行标志
     */
    private String[] bocFlag;

    public String[] getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String[] bocFlag) {
        this.bocFlag = bocFlag;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    List<PayeeEntity> mPayeeEntityList = new ArrayList<PayeeEntity>();

    public List<PayeeEntity> getPayeeEntityList() {
        return mPayeeEntityList;
    }

    public void setPayeeEntityList(List<PayeeEntity> payeeEntityList) {
        mPayeeEntityList = payeeEntityList;
    }

    public static class PayeeEntity {
        /**
         * 收款人ID
         */
        private String payeeId;
        /**
         * 收款人名称
         */
        private String accountName;
        /**
         * 收款人账号
         */
        private String accountNumber;
        /**
         * 收款人账户类型
         */
        private String accountType;
        /**
         * 省行联行号
         */
        private String cnapsCode;
        /**
         * 收款账户开户行(非关联信用卡定向还款不需要此字段)
         */
        private String bankName;
        /**
         * 收款账户开户行(跨行定行汇款才需要此字段)
         */
        private String address;
        /**
         * 收款人手机号
         */
        private String mobile;
        /**
         * 收款人所属地区(非关联信用卡定向还款不需要此字段)
         */
        private String accountIbkNum;
        /**
         * 收款人账户所属行
         */
        private String bankCode;
        /**
         * 收款人标志
         */
        private String bocFlag;

        public String getPayeeId() {
            return payeeId;
        }

        public void setPayeeId(String payeeId) {
            this.payeeId = payeeId;
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

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getBocFlag() {
            return bocFlag;
        }

        public void setBocFlag(String bocFlag) {
            this.bocFlag = bocFlag;
        }
    }
}
