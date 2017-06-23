package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 投资协议申请
 * Created by zhx on 2016/11/10
 */
public class XpadApplyAgreementResultViewModel implements Parcelable {
    //产品代码
    private String productCode;
    //投资方式
    private String investType;
    //钞汇标识
    private String xpadCashRemit;
    //总期数
//    private String totalPeriod;
    //投资时间
    private String investTime;
    //产品币种
    private String curCode;
    //产品代码
    private String prodName;
    //定投类型
    private String timeInvestType;
    //定投金额
    private String redeemAmount;
    //定投频率
    private String timeInvestRate;
    //定投频率类型 定投频率类型须输入格式为字母,如'w'表示周, d:天, w:周, m:月, y:年；
    private String timeInvestRateFlag;
    //最低限额
//    private String minAmount;
    //最高限额
//    private String maxAmount;
    //账户标识;
    private String accountId;
    //防重标识
    private String token;
    //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getTimeInvestType() {
        return timeInvestType;
    }

    public void setTimeInvestType(String timeInvestType) {
        this.timeInvestType = timeInvestType;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getTimeInvestRate() {
        return timeInvestRate;
    }

    public void setTimeInvestRate(String timeInvestRate) {
        this.timeInvestRate = timeInvestRate;
    }

    public String getTimeInvestRateFlag() {
        return timeInvestRateFlag;
    }

    public void setTimeInvestRateFlag(String timeInvestRateFlag) {
        this.timeInvestRateFlag = timeInvestRateFlag;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    private String period;
    private String periodType;
    private String transSeq;
    private String transType;
    private String memo;
    private String cashRemit;
    private String firstDate;
    private String amountType;
    private String addAmount;
    private String contAmtMode;
    private String jobType;
    private String serialCode;
    private String serialName;
    private String totalPeriod;
    private String surplusPeriod;
    private String startPeriod;
    private String endPeriod;
    private String minAmount;
    private String maxAmount;
    private String contractSeq;
    private String contFlag;
    private String contStatus;
    private String operateDate;
    private String xpadAccountNo;
    private String baseAmount;
    private String proCur;
    private String serialPrdct;
    private String issuePeriod;
    private String periodSeq;
    private String lastDate;
    private String lastStatus;
    private String canUpdate;
    private String canPause;
    private String canStart;
    private String canCancel;
    private String periodSeqType;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(String addAmount) {
        this.addAmount = addAmount;
    }

    public String getContAmtMode() {
        return contAmtMode;
    }

    public void setContAmtMode(String contAmtMode) {
        this.contAmtMode = contAmtMode;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getSurplusPeriod() {
        return surplusPeriod;
    }

    public void setSurplusPeriod(String surplusPeriod) {
        this.surplusPeriod = surplusPeriod;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getContractSeq() {
        return contractSeq;
    }

    public void setContractSeq(String contractSeq) {
        this.contractSeq = contractSeq;
    }

    public String getContFlag() {
        return contFlag;
    }

    public void setContFlag(String contFlag) {
        this.contFlag = contFlag;
    }

    public String getContStatus() {
        return contStatus;
    }

    public void setContStatus(String contStatus) {
        this.contStatus = contStatus;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getXpadAccountNo() {
        return xpadAccountNo;
    }

    public void setXpadAccountNo(String xpadAccountNo) {
        this.xpadAccountNo = xpadAccountNo;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getProCur() {
        return proCur;
    }

    public void setProCur(String proCur) {
        this.proCur = proCur;
    }

    public String getSerialPrdct() {
        return serialPrdct;
    }

    public void setSerialPrdct(String serialPrdct) {
        this.serialPrdct = serialPrdct;
    }

    public String getIssuePeriod() {
        return issuePeriod;
    }

    public void setIssuePeriod(String issuePeriod) {
        this.issuePeriod = issuePeriod;
    }

    public String getPeriodSeq() {
        return periodSeq;
    }

    public void setPeriodSeq(String periodSeq) {
        this.periodSeq = periodSeq;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getCanPause() {
        return canPause;
    }

    public void setCanPause(String canPause) {
        this.canPause = canPause;
    }

    public String getCanStart() {
        return canStart;
    }

    public void setCanStart(String canStart) {
        this.canStart = canStart;
    }

    public String getCanCancel() {
        return canCancel;
    }

    public void setCanCancel(String canCancel) {
        this.canCancel = canCancel;
    }

    public String getPeriodSeqType() {
        return periodSeqType;
    }

    public void setPeriodSeqType(String periodSeqType) {
        this.periodSeqType = periodSeqType;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.investType);
        dest.writeString(this.xpadCashRemit);
        dest.writeString(this.investTime);
        dest.writeString(this.curCode);
        dest.writeString(this.prodName);
        dest.writeString(this.timeInvestType);
        dest.writeString(this.redeemAmount);
        dest.writeString(this.timeInvestRate);
        dest.writeString(this.timeInvestRateFlag);
        dest.writeString(this.accountId);
        dest.writeString(this.token);
        dest.writeString(this.conversationId);
        dest.writeString(this.period);
        dest.writeString(this.periodType);
        dest.writeString(this.transSeq);
        dest.writeString(this.transType);
        dest.writeString(this.memo);
        dest.writeString(this.cashRemit);
        dest.writeString(this.firstDate);
        dest.writeString(this.amountType);
        dest.writeString(this.addAmount);
        dest.writeString(this.contAmtMode);
        dest.writeString(this.jobType);
        dest.writeString(this.serialCode);
        dest.writeString(this.serialName);
        dest.writeString(this.totalPeriod);
        dest.writeString(this.surplusPeriod);
        dest.writeString(this.startPeriod);
        dest.writeString(this.endPeriod);
        dest.writeString(this.minAmount);
        dest.writeString(this.maxAmount);
        dest.writeString(this.contractSeq);
        dest.writeString(this.contFlag);
        dest.writeString(this.contStatus);
        dest.writeString(this.operateDate);
        dest.writeString(this.xpadAccountNo);
        dest.writeString(this.baseAmount);
        dest.writeString(this.proCur);
        dest.writeString(this.serialPrdct);
        dest.writeString(this.issuePeriod);
        dest.writeString(this.periodSeq);
        dest.writeString(this.lastDate);
        dest.writeString(this.lastStatus);
        dest.writeString(this.canUpdate);
        dest.writeString(this.canPause);
        dest.writeString(this.canStart);
        dest.writeString(this.canCancel);
        dest.writeString(this.periodSeqType);
    }

    public XpadApplyAgreementResultViewModel() {
    }

    private XpadApplyAgreementResultViewModel(Parcel in) {
        this.productCode = in.readString();
        this.investType = in.readString();
        this.xpadCashRemit = in.readString();
        this.investTime = in.readString();
        this.curCode = in.readString();
        this.prodName = in.readString();
        this.timeInvestType = in.readString();
        this.redeemAmount = in.readString();
        this.timeInvestRate = in.readString();
        this.timeInvestRateFlag = in.readString();
        this.accountId = in.readString();
        this.token = in.readString();
        this.conversationId = in.readString();
        this.period = in.readString();
        this.periodType = in.readString();
        this.transSeq = in.readString();
        this.transType = in.readString();
        this.memo = in.readString();
        this.cashRemit = in.readString();
        this.firstDate = in.readString();
        this.amountType = in.readString();
        this.addAmount = in.readString();
        this.contAmtMode = in.readString();
        this.jobType = in.readString();
        this.serialCode = in.readString();
        this.serialName = in.readString();
        this.totalPeriod = in.readString();
        this.surplusPeriod = in.readString();
        this.startPeriod = in.readString();
        this.endPeriod = in.readString();
        this.minAmount = in.readString();
        this.maxAmount = in.readString();
        this.contractSeq = in.readString();
        this.contFlag = in.readString();
        this.contStatus = in.readString();
        this.operateDate = in.readString();
        this.xpadAccountNo = in.readString();
        this.baseAmount = in.readString();
        this.proCur = in.readString();
        this.serialPrdct = in.readString();
        this.issuePeriod = in.readString();
        this.periodSeq = in.readString();
        this.lastDate = in.readString();
        this.lastStatus = in.readString();
        this.canUpdate = in.readString();
        this.canPause = in.readString();
        this.canStart = in.readString();
        this.canCancel = in.readString();
        this.periodSeqType = in.readString();
    }

    public static final Parcelable.Creator<XpadApplyAgreementResultViewModel> CREATOR = new Parcelable.Creator<XpadApplyAgreementResultViewModel>() {
        public XpadApplyAgreementResultViewModel createFromParcel(Parcel source) {
            return new XpadApplyAgreementResultViewModel(source);
        }

        public XpadApplyAgreementResultViewModel[] newArray(int size) {
            return new XpadApplyAgreementResultViewModel[size];
        }
    };
}
