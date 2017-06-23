package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 由076接口 PsnFincAccountList 获取资金账号列表
 * 返回：账户nickName:长城电子借记卡
 * 返回：账号accounrNumber
 * Created by lyf7084 on 2016/12/9.
 */
public class ChangeCardModel implements Serializable {
    /**
     * 变更资金帐户列表
     * 由076查回AccountID是int，而在049接口提交时AccountID String
     * 故再请求成功后给Model赋值先转为String
     */
    private List<CardListBean> cardList;

    public List<CardListBean> getCardList() {
        return cardList;
    }

    public void setcardList(List<CardListBean> setcardList) {
        this.cardList = setcardList;
    }

    public class CardListBean {
        /**
         * 账户别名：长城电子借记卡
         */
        private String nickName;

        /**
         * 账号：记得账户格式化
         */
        private String accountNumber;

        /**
         *
         */
        private String accountType;
        /**
         *
         */
        private String accountName;


        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        private String accountId;


        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getNickName() {
            return nickName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}
