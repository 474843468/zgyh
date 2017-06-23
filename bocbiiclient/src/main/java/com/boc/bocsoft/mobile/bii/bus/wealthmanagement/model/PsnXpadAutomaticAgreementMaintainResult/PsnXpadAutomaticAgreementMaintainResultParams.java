package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult;

/**
 * 协议维护
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadAutomaticAgreementMaintainResultParams {
    private String contractSeq;//	协议序号
    private String serialCode;//	产品代码
    private String serialName;//	产品代码
    private String curCode;//	币种
    private String cashRemit;//	钞汇标识
    private String agreementType;//	投资方式
    private String maintainFlag;//	维护标识
    private String periodType;//	定投类型
    private String totalPeriod;//	总期数
    private String baseAmount;//	投资份额
    private String periodSeq;//	定投频率
    private String periodSeqType;//	定投频率类型
    private String lastDate;//	定投日期
    private String minAmount;//	最低限额
    private String maxAmount;//	最高限额
    private String accountKey;//	账号缓存标识
    private String token;//	防重标识
    private String conversationId;//	回话id

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getContractSeq() {
        return contractSeq;
    }

    public void setContractSeq(String contractSeq) {
        this.contractSeq = contractSeq;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getMaintainFlag() {
        return maintainFlag;
    }

    public void setMaintainFlag(String maintainFlag) {
        this.maintainFlag = maintainFlag;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getPeriodSeq() {
        return periodSeq;
    }

    public void setPeriodSeq(String periodSeq) {
        this.periodSeq = periodSeq;
    }

    public String getPeriodSeqType() {
        return periodSeqType;
    }

    public void setPeriodSeqType(String periodSeqType) {
        this.periodSeqType = periodSeqType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }
}
