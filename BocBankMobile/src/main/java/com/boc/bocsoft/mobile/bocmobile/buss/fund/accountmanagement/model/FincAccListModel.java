package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import java.util.List;

/**
 * 由076接口 PsnFincAccountList 获取资金账号列表
 * 上送参数：空
 * 返回参数：List<FincAccountBean>
 * Created by lyf7084 on 2016/12/13.
 */
public class FincAccListModel {

    private List<FincAccountBean> list;

    public List<FincAccountBean> getList() {
        return list;
    }

    public void setList(List<FincAccountBean> list) {
        this.list = list;
    }

    public class FincAccountBean {


        /**
         * 账户名称
         */
        private String accountName;

        /**
         * 新资金账号
         */
        private String nickName;

        /**
         * 新资金账号
         */
        private String accountType;

        /**
         * 账号
         */
        private String accountNumber;

        /**
         * 账户状态
         */
        private String accountStatus;

        /**
         * 网银账户标识
         */
        private int accountId;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

    }
}
