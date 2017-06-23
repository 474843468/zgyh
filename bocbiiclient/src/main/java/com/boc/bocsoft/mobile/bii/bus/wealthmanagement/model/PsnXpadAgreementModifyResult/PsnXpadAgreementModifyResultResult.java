package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult;

/**
 * 协议修改结果
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadAgreementModifyResultResult {
    private String transactionId;//	网银交易序号
    private String operateDate;//	协议修改日期
    private String surplusPeriod;//	剩余期数
    private String startPeriod;//	开始期数
    private String endPeriod;//	结束期数

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getSurplusPeriod() {
        return surplusPeriod;
    }

    public void setSurplusPeriod(String surplusPeriod) {
        this.surplusPeriod = surplusPeriod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
