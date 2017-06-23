package com.boc.bocma.serviceinterface.op.interfacemodel.common;

import android.text.TextUtils;

/**
 * 借记卡基本信息存储：卡号，唯一标识
 */
public class MAOPBankCard {
    private String cardNumber;
    private String identifier;
    private double balance;
    private String orgidname;
    
    public static final int DEBIT_CARD = 1;
    public static final int CREDIT_CARD = 2;
    /**
     * 借记卡卡号长度
     */
    public static final int DEBIT_CARD_NUMBER_LENGTH = 19;
    
    public int getCardType() {
        if (!TextUtils.isEmpty(cardNumber)
                && cardNumber.length() == DEBIT_CARD_NUMBER_LENGTH) {
            return DEBIT_CARD;
        }
        return CREDIT_CARD;
    }
    
    /**
     * @return 卡号
     */
    public String getCardNumber() {
        return cardNumber;
    }
    
    /**
     * @param cardNumber 卡号
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    /**
     * @return 卡唯一标识
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * @param identifier 卡唯一标识
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * @return 卡余额
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * @param balance 卡余额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return 开卡机构名称
     */
	public String getOrgidname() {
		return orgidname;
	}

	/**
	 * 
	 * @param orgidname 开卡机构名称
	 */
	public void setOrgidname(String orgidname) {
		this.orgidname = orgidname;
	}
    
    
}
