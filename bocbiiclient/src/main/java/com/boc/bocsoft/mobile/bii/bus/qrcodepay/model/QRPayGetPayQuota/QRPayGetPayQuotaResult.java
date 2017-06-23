package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota;

/**
 * 查询支付限额
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetPayQuotaResult {


    //信用卡支付单笔限额
    private String creditCardTransQuota;
    //信用卡支付日限额
    private String creditCardDailyQuota;
    //信用卡支付月限额
    private String creditCardMonthlyQuota;
    //借记卡支付单笔限额
    private String debitCardTransQuota;
    //借记卡支付日限额
    private String debitCardDailyQuota;
    //借记卡支付月限额
    private String debitCardMonthlyQuota;

    //606
    //支付单笔限额 - 信用卡支付单笔限额
    private String cardTransQuota;
    //借记卡支付单笔限额
    private String cardPayQuota;
    //借记卡转账单笔限额
    private String cardTransferQuota;



    public String getCreditCardTransQuota() {
        return creditCardTransQuota;
    }

    public void setCreditCardTransQuota(String creditCardTransQuota) {
        this.creditCardTransQuota = creditCardTransQuota;
    }

    public String getCreditCardDailyQuota() {
        return creditCardDailyQuota;
    }

    public void setCreditCardDailyQuota(String creditCardDailyQuota) {
        this.creditCardDailyQuota = creditCardDailyQuota;
    }

    public String getCreditCardMonthlyQuota() {
        return creditCardMonthlyQuota;
    }

    public void setCreditCardMonthlyQuota(String creditCardMonthlyQuota) {
        this.creditCardMonthlyQuota = creditCardMonthlyQuota;
    }

    public String getDebitCardTransQuota() {
        return debitCardTransQuota;
    }

    public void setDebitCardTransQuota(String debitCardTransQuota) {
        this.debitCardTransQuota = debitCardTransQuota;
    }

    public String getDebitCardDailyQuota() {
        return debitCardDailyQuota;
    }

    public void setDebitCardDailyQuota(String debitCardDailyQuota) {
        this.debitCardDailyQuota = debitCardDailyQuota;
    }

    public String getDebitCardMonthlyQuota() {
        return debitCardMonthlyQuota;
    }

    public void setDebitCardMonthlyQuota(String debitCardMonthlyQuota) {
        this.debitCardMonthlyQuota = debitCardMonthlyQuota;
    }

    public String getCardTransQuota() {
        return cardTransQuota;
    }

    public void setCardTransQuota(String cardTransQuota) {
        this.cardTransQuota = cardTransQuota;
    }

    public String getCardPayQuota() {
        return cardPayQuota;
    }

    public void setCardPayQuota(String cardPayQuota) {
        this.cardPayQuota = cardPayQuota;
    }

    public String getCardTransferQuota() {
        return cardTransferQuota;
    }

    public void setCardTransferQuota(String cardTransferQuota) {
        this.cardTransferQuota = cardTransferQuota;
    }
}
