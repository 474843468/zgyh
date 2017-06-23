package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail;

import java.util.List;

/**
 * Created by feib on 16/8/2.
 */
public class PsnAccBocnetQryDebitDetailResult {


    private String openDate;
    private String accountIbkNum;
    private String accountType;
    private String accountStatus;
    private String openBank;

    private List<AccountDetaiListBean> accountDetaiList;

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public List<AccountDetaiListBean> getAccountDetaiList() {
        return accountDetaiList;
    }

    public void setAccountDetaiList(List<AccountDetaiListBean> accountDetaiList) {
        this.accountDetaiList = accountDetaiList;
    }

    public static class AccountDetaiListBean {
        private String currencyCode;
        private String cashRemit;
        private String bookBalance;
        private String availableBalance;


        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(String bookBalance) {
            this.bookBalance = bookBalance;
        }

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }
    }
}
