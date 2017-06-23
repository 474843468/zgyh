package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardRelevance;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/26 16:55
 *         虚拟银行卡关联网银
 */
public class PsnCrcdVirtualCardRelevanceParams extends PublicParams{

    /** 账户标识 */
    private String accountId;
    /** 实体卡号 */
    private String accNum;
    /** 虚拟卡卡号 */
    private String virtualCardNo;
    /** 信用卡的电话银行密码 */
    private String phoneBankPassword;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getVirtualCardNo() {
        return virtualCardNo;
    }

    public void setVirtualCardNo(String virtualCardNo) {
        this.virtualCardNo = virtualCardNo;
    }

    public String getPhoneBankPassword() {
        return phoneBankPassword;
    }

    public void setPhoneBankPassword(String phoneBankPassword) {
        this.phoneBankPassword = phoneBankPassword;
    }
}
