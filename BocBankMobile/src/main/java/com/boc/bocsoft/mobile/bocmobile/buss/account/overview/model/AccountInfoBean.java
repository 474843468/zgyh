package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model;

/**
 * @author wangyang
 *         16/8/17 16:01
 *         账户开户行,开户信息等内容
 */
public class AccountInfoBean {

    /** 开户行信息 */
    private String accOpenBank;
    /** 开户时间 */
    private String accOpenDate;
    /** 钞/汇 只有普活时候需要 */
    private String cashRemit;

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public String getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(String accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }
}
