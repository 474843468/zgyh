package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 21:06
 * 描述：
 */
public interface IPledgeLoanInfoFillViewModel extends BaseFillInfoBean {

    String getCombinName();

    void setCombinName(String combinName);

    String get_combinId();

    void set_combinId(String _combinId);

    String getLoanType();

    void setLoanType(String loanType);

    String getAvailableAmount();

    void setAvailableAmount(String availableAmount);

    String getAmount();

    void setAmount(String amount);

    String getCurrencyCode();

    void setCurrencyCode(String currencyCode);

    String getLoanPeriod();

    void setLoanPeriod(String loanPeriod);

    String getLoanRate();

    void setLoanRate(String loanRate);

    String getPayType();

    void setPayType(String payType);

    String getPayCycle();

    void setPayCycle(String payCycle);

    String getToActNum();

    void setToActNum(String toActNum);

    String getToAccount();

    void setToAccount(String toAccount);

    String getPayAccount();

    void setPayAccount(String payAccount);

    String getConversationId();

    void setConversationId(String conversationId);

    String getPayActNum();

    void setPayActNum(String payActNum);

    String getPayTypeString();

    String getPayCycleString();
}
