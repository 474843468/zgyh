package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail;

import java.util.List;

/**
 * 047 查询历史交易信息-返回参数
 * Created by wy7105 on 2016/11/24.
 */
public class PsnFundQueryHistoryDetailResult {

    private int recordNumber; //记录条数
    private List<ListEntity> list; //历史交易列表

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        /**
         * confirmAmount : 1000.00
         * unConfirmCount : 0.00
         * inFncode :
         * applyCount : 0.00
         * regCode : null
         * appointDate : 2015/09/16
         * applyAmount : 1000.00
         * transSeq : null
         * regName : null
         * fundCode : 690004
         * cashFlag : CAS
         * transDate : 2015/09/16
         * confirmCount : 0.00
         * failReason :
         * transCode : 120
         * bonusType :
         * transStatus : 0
         * taAccount : null
         * inFnname :
         * netValue : 11.6070
         * transFee : 1.00
         * specialTransFlag : 0
         * confirmDate : Thu Sep 17 00:00:00 GMT+08:00 2015
         * isCancle :
         * unConfirmAmount : 0.00
         * fundName : 民生稳健
         * currencyCode : 001
         */
        private String confirmAmount; //成交金额
        private String unConfirmCount; //未成交份额
        private String inFncode; //转入基金代码
        private String applyCount; //申请份额
        private String regCode; //注册基金公司代码
        private String appointDate; //指定交易日期
        private String applyAmount; //申请金额
        private String transSeq; //基金交易流水号
        private String regName; //注册基金公司名称
        private String fundCode; //基金代码
        private String cashFlag; //钞汇
        private String transDate; //交易日期
        private String confirmCount; //成交份额
        private String failReason; //失败原因
        private String transCode; //交易码
        private String bonusType; //分红方式
        private String transStatus; //交易状态
        private String taAccount; //TA账号
        private String inFnname; //转入基金代码
        private String netValue; //基金净值
        private String transFee; //交易费用
        private String specialTransFlag; //specialTransFlag
        private String confirmDate; //确认日期
        private String isCancle; //是否撤销
        private String unConfirmAmount; //未成交金额
        private String fundName; //基金名称
        private String currencyCode; //币种

        public void setConfirmAmount(String confirmAmount) {
            this.confirmAmount = confirmAmount;
        }

        public void setUnConfirmCount(String unConfirmCount) {
            this.unConfirmCount = unConfirmCount;
        }

        public void setInFncode(String inFncode) {
            this.inFncode = inFncode;
        }

        public void setApplyCount(String applyCount) {
            this.applyCount = applyCount;
        }

        public void setRegCode(String regCode) {
            this.regCode = regCode;
        }

        public void setAppointDate(String appointDate) {
            this.appointDate = appointDate;
        }

        public void setApplyAmount(String applyAmount) {
            this.applyAmount = applyAmount;
        }

        public void setTransSeq(String transSeq) {
            this.transSeq = transSeq;
        }

        public void setRegName(String regName) {
            this.regName = regName;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public void setConfirmCount(String confirmCount) {
            this.confirmCount = confirmCount;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public void setBonusType(String bonusType) {
            this.bonusType = bonusType;
        }

        public void setTransStatus(String transStatus) {
            this.transStatus = transStatus;
        }

        public void setTaAccount(String taAccount) {
            this.taAccount = taAccount;
        }

        public void setInFnname(String inFnname) {
            this.inFnname = inFnname;
        }

        public void setNetValue(String netValue) {
            this.netValue = netValue;
        }

        public void setTransFee(String transFee) {
            this.transFee = transFee;
        }

        public void setSpecialTransFlag(String specialTransFlag) {
            this.specialTransFlag = specialTransFlag;
        }

        public void setConfirmDate(String confirmDate) {
            this.confirmDate = confirmDate;
        }

        public void setIsCancle(String isCancle) {
            this.isCancle = isCancle;
        }

        public void setUnConfirmAmount(String unConfirmAmount) {
            this.unConfirmAmount = unConfirmAmount;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getConfirmAmount() {
            return confirmAmount;
        }

        public String getUnConfirmCount() {
            return unConfirmCount;
        }

        public String getInFncode() {
            return inFncode;
        }

        public String getApplyCount() {
            return applyCount;
        }

        public String getRegCode() {
            return regCode;
        }

        public String getAppointDate() {
            return appointDate;
        }

        public String getApplyAmount() {
            return applyAmount;
        }

        public String getTransSeq() {
            return transSeq;
        }

        public String getRegName() {
            return regName;
        }

        public String getFundCode() {
            return fundCode;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public String getTransDate() {
            return transDate;
        }

        public String getConfirmCount() {
            return confirmCount;
        }

        public String getFailReason() {
            return failReason;
        }

        public String getTransCode() {
            return transCode;
        }

        public String getBonusType() {
            return bonusType;
        }

        public String getTransStatus() {
            return transStatus;
        }

        public String getTaAccount() {
            return taAccount;
        }

        public String getInFnname() {
            return inFnname;
        }

        public String getNetValue() {
            return netValue;
        }

        public String getTransFee() {
            return transFee;
        }

        public String getSpecialTransFlag() {
            return specialTransFlag;
        }

        public String getConfirmDate() {
            return confirmDate;
        }

        public String getIsCancle() {
            return isCancle;
        }

        public String getUnConfirmAmount() {
            return unConfirmAmount;
        }

        public String getFundName() {
            return fundName;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }
    }
}
