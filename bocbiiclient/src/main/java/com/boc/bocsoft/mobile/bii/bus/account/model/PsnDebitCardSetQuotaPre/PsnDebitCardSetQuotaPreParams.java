package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuotaPre;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         2016/10/10 20:39
 *         设置交易限额预交易参数
 */
public class PsnDebitCardSetQuotaPreParams extends PublicParams{

    /** 借记卡账户标识 */
    private String accountId;
    /** 每日转账交易限额 */
    private String transDay;
    /** 每日POS交易限额 */
    private String allDayPOS;
    /** 境内ATM取现日限额 */
    private String cashDayATM;
    /** 境外POS消费限额 */
    private String consumeForeignPOS;
    /** 境外ATM取款日限额 */
    private String cashDayForeignATM;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTransDay() {
        return transDay;
    }

    public void setTransDay(String transDay) {
        this.transDay = transDay;
    }

    public String getAllDayPOS() {
        return allDayPOS;
    }

    public void setAllDayPOS(String allDayPOS) {
        this.allDayPOS = allDayPOS;
    }

    public String getCashDayATM() {
        return cashDayATM;
    }

    public void setCashDayATM(String cashDayATM) {
        this.cashDayATM = cashDayATM;
    }

    public String getConsumeForeignPOS() {
        return consumeForeignPOS;
    }

    public void setConsumeForeignPOS(String consumeForeignPOS) {
        this.consumeForeignPOS = consumeForeignPOS;
    }

    public String getCashDayForeignATM() {
        return cashDayForeignATM;
    }

    public void setCashDayForeignATM(String cashDayForeignATM) {
        this.cashDayForeignATM = cashDayForeignATM;
    }
}
