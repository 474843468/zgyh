package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;


import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/22.
 * 中银E贷查询签约额度列表ui
 */
public class EloanQuoteViewModel implements Serializable{

    /**贷款利率*/
    private String rate;
    /**还款账户*/
    private String repayAcct;
    /**贷款品种*/
    private String loanType;
    /**额度号码*/
    private String quoteNo;
    /**额度状态 05:正常 10：取消 40：关闭 20：冻结*/
    private String quoteState;
    /**还款日*/
    private String issueRepayDate;
    /**额度到期日*/
    private String loanToDate;
    /**签约类型  01：WLCF 02：PLCF 03: 账户签约*/
    private String quoteType;
    /**签约状态  0：有效 9：无效*/
    private String registerStates;
    /**币种*/
    private String currency;
    /**额度金额*/
    private String loanBanlance;
    /**可用金额*/
    private String availableAvl;
    /**已用金额*/
    private String useAvl;
    /**下次还款日标示  0：第一个还款日还款（非整期后）
     1：一个整期后还款日还款
     3：按标准日结息
     */
    private String nextRepayDate;

    /**支付签约借记卡卡号*/
    private String signAccountNum;
    /**支付签约最小放款金额*/
    private String minLoanAmount;
    /**支付签约最小放款金额的币种*/
    private String minLoanAmtCur;
    /**用款偏好*/
    private String usePref;
    /**期限*/
    private String signPeriod;
    /**还款方式*/
    private String payType;
    /**签约日期*/
    private String signDate;
    /**支付签约日期*/
    private String paySignDate;
    /**支付解约日期*/
    private String payUnsignDate;

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public String getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(String minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public String getMinLoanAmtCur() {
        return minLoanAmtCur;
    }

    public void setMinLoanAmtCur(String minLoanAmtCur) {
        this.minLoanAmtCur = minLoanAmtCur;
    }

    public String getUsePref() {
        return usePref;
    }

    public void setUsePref(String usePref) {
        this.usePref = usePref;
    }

    public String getSignPeriod() {
        return signPeriod;
    }

    public void setSignPeriod(String signPeriod) {
        this.signPeriod = signPeriod;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getPaySignDate() {
        return paySignDate;
    }

    public void setPaySignDate(String paySignDate) {
        this.paySignDate = paySignDate;
    }

    public String getPayUnsignDate() {
        return payUnsignDate;
    }

    public void setPayUnsignDate(String payUnsignDate) {
        this.payUnsignDate = payUnsignDate;
    }

    public String getRate() {
        return MoneyUtils.transRateFormat(rate);
    }

    public String getRepayAcct() {
        return repayAcct;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public String getQuoteState() {
        return quoteState;
    }

    public String getIssueRepayDate() {
        return issueRepayDate;
    }

    public String getLoanToDate() {
        return loanToDate;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public String getRegisterStates() {
        return registerStates;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLoanBanlance() {
        return loanBanlance;
    }

    public String getAvailableAvl() {
        return availableAvl;
    }

    public String getUseAvl() {
        return useAvl;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public void setRate(String rate) {
        rate = MoneyUtils.transRateFormat(rate);
        this.rate = rate;
    }

    public void setRepayAcct(String repayAcct) {
        this.repayAcct = repayAcct;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public void setQuoteState(String quoteState) {
        this.quoteState = quoteState;
    }

    public void setIssueRepayDate(String issueRepayDate) {
        this.issueRepayDate = issueRepayDate;
    }

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public void setRegisterStates(String registerStates) {
        this.registerStates = registerStates;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLoanBanlance(String loanBanlance) {
        this.loanBanlance = loanBanlance;
    }

    public void setAvailableAvl(String availableAvl) {
        this.availableAvl = availableAvl;
    }

    public void setUseAvl(String useAvl) {
        this.useAvl = useAvl;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    @Override
    public String toString() {
        return "EquoteDetailBean{" +
                "rate='" + rate + '\'' +
                ", repayAcct='" + repayAcct + '\'' +
                ", loanType='" + loanType + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", quoteState='" + quoteState + '\'' +
                ", issueRepayDate='" + issueRepayDate + '\'' +
                ", loanToDate='" + loanToDate + '\'' +
                ", quoteType='" + quoteType + '\'' +
                ", registerStates='" + registerStates + '\'' +
                ", currency='" + currency + '\'' +
                ", loanBanlance='" + loanBanlance + '\'' +
                ", availableAvl='" + availableAvl + '\'' +
                ", useAvl='" + useAvl + '\'' +
                ", nextRepayDate='" + nextRepayDate + '\'' +
                '}';
    }

}
