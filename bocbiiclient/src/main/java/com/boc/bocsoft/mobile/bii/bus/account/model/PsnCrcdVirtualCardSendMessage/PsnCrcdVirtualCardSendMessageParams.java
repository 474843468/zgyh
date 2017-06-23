package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSendMessage;

/**
 * @author wangyang
 *         16/7/26 14:43
 *         虚拟银行卡发送短信
 */
public class PsnCrcdVirtualCardSendMessageParams {

    /** 账户标识 */
    private String accountId;
    /** 虚拟卡卡号 */
    private String virCardNo;

    public PsnCrcdVirtualCardSendMessageParams(String accountId, String virCardNo) {
        this.accountId = accountId;
        this.virCardNo = virCardNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getVirCardNo() {
        return virCardNo;
    }

    public void setVirCardNo(String virCardNo) {
        this.virCardNo = virCardNo;
    }
}
