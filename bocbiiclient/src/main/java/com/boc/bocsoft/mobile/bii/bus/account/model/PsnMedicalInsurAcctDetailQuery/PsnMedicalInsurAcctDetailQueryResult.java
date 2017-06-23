package com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 医保账户详情查询返回报文
 * Created by niuguobin on 2016/6/23.
 */
public class PsnMedicalInsurAcctDetailQueryResult {
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 开户日期
     * YYYY/MM/DD
     */
    private LocalDate accOpenDate;
    /**
     * 开户行
     */
    private String accOpenBank;
    /**
     * 账户详情列表
     */
    private List<AccountDetai> accountDetaiList;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDate getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(LocalDate accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public List<AccountDetai> getAccountDetaiList() {
        return accountDetaiList;
    }

    public void setAccountDetaiList(List<AccountDetai> accountDetaiList) {
        this.accountDetaiList = accountDetaiList;
    }
}
