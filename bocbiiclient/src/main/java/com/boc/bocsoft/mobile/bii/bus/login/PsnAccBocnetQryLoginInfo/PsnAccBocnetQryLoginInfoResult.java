package com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo;

/**
 * Created by feib on 16/8/2.
 */
public class PsnAccBocnetQryLoginInfoResult {

    /**
     * accountSeq : 127152
     * accountType : 103
     * name : 李小小
     * eBankingFlag : L
     * isHaveEleCashAcct : false
     * accountNumber : 4096688381456341
     */

    private String accountSeq;
    private String accountType;
    private String name;
    private String eBankingFlag;
    private String isHaveEleCashAcct;
    private String accountNumber;

    public String getAccountSeq() {
        return accountSeq;
    }

    public void setAccountSeq(String accountSeq) {
        this.accountSeq = accountSeq;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEBankingFlag() {
        return eBankingFlag;
    }

    public void setEBankingFlag(String eBankingFlag) {
        this.eBankingFlag = eBankingFlag;
    }

    public String isIsHaveEleCashAcct() {
        return isHaveEleCashAcct;
    }

    public void setIsHaveEleCashAcct(String isHaveEleCashAcct) {
        this.isHaveEleCashAcct = isHaveEleCashAcct;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
