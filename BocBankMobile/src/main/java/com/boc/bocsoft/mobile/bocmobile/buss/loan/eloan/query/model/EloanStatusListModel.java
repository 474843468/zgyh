package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/27.
 * 网络请求公共封装类
 */
public class EloanStatusListModel implements Serializable{

    /**
     * 额度状态
     */
    private String quoteState;
    /**
     * 签约类型 01：WLCF 02：PLCF 03: 账户签约
     */
    private String quoteType;
    /**
     * 额度金额
     */
    private String loanBanlance;
    /**
     * 可用金额
     */
    private String availableAvl;
    /**
     * 已用金额
     */
    private String useAvl;
    /**
     * 额度到期日
     */
    private String loanToDate;
    /**
     * 已结清 ，未结清
     */
    private boolean isClear;
    /**利率*/
    private String rate;

    /**额度编号*/
    private String quoteNo;

    /**下次还款日标识*/
    private String nextRepayDate;

    /**贷款类型*/
    private String loanType;
    /**币种*/
    private String currencyCode;
    /**还款账户*/
    private String payAccount;
    /**还款日*/
    private String issueRepayDate;
    /**签约对象*/
    private EloanQuoteViewModel quoteViewModel;
    /**提款对象*/
    private EloanAccountListModel accountListModel;

    public EloanQuoteViewModel getQuoteViewModel() {
        return quoteViewModel;
    }

    public void setQuoteViewModel(EloanQuoteViewModel quoteViewModel) {
        this.quoteViewModel = quoteViewModel;
    }

    public EloanAccountListModel getAccountListModel() {
        return accountListModel;
    }

    public void setAccountListModel(EloanAccountListModel accountListModel) {
        this.accountListModel = accountListModel;
    }

    public String getQuoteState() {
        return quoteState;
    }

    public String getQuoteType() {
        return quoteType;
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

    public String getLoanToDate() {
        return loanToDate;
    }

    public boolean isClear() {
        return isClear;
    }

    public String getRate() {
        return MoneyUtils.transRateFormat(rate);
    }

    public void setQuoteState(String quoteState) {
        this.quoteState = quoteState;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
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

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public void setRate(String rate) {
        rate = MoneyUtils.transRateFormat(rate);
        this.rate = rate;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public String getIssueRepayDate() {
        return issueRepayDate;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public void setIssueRepayDate(String issueRepayDate) {
        this.issueRepayDate = issueRepayDate;
    }

    @Override
    public String toString() {
        return "EloanStatusListModel{" +
                "quoteState='" + quoteState + '\'' +
                ", quoteType='" + quoteType + '\'' +
                ", loanBanlance='" + loanBanlance + '\'' +
                ", availableAvl='" + availableAvl + '\'' +
                ", useAvl='" + useAvl + '\'' +
                ", loanToDate='" + loanToDate + '\'' +
                ", isClear=" + isClear +
                ", rate='" + rate + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", nextRepayDate='" + nextRepayDate + '\'' +
                ", loanType='" + loanType + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", issueRepayDate='" + issueRepayDate + '\'' +
                ", accountListModel=" + accountListModel +
                '}';
    }

}
