package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/21.
 * 046 PsnFundQueryTransOntran基金在途交易查询
 */

public class PsnFundQueryTransOntranResult {

    /**返回数据总条目*/
    private int recordNumber;

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**币种*/
        private String currencyCode;
        /**委托日期*/
        private String paymentDate;
        /**交易金额*/
        private String transAmount;
        /**基金代码*/
        private String fundCode;
        /**基金名称*/
        private String fundName;
        /**指定交易日期*/
        private String appointDate;
        /**交易份额*/
        private String transCount;
        /**交易类型*/
        private String fundTranType;
        /**分红方式*/
        private String bonusType;
        /**交易状态*/
        private String transStatus;
        /**委托日期*/
        private String cashFlag;
        /**特殊交易标示*/
        private String specialTransFlag;
        /**注册基金公司代码*/
        private String fundRegCode;
        /**注册基金公司名称*/
        private String fundRegName;
        /**TA 账号*/
        private String taAccountNo;
        /**确认日期*/
        private String confirmDate;
        /**转入基金代码*/
        private String inFundCode;
        /**转入基金名称*/
        private String inFundName;
        /**是否可撤销*/
        private String cancleFlag;
        /**原基金交易流水号*/
        private String orignFundSeq;
        
        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getTransAmount() {
            return transAmount;
        }

        public void setTransAmount(String transAmount) {
            this.transAmount = transAmount;
        }

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getAppointDate() {
            return appointDate;
        }

        public void setAppointDate(String appointDate) {
            this.appointDate = appointDate;
        }

        public String getTransCount() {
            return transCount;
        }

        public void setTransCount(String transCount) {
            this.transCount = transCount;
        }

        public String getFundTranType() {
            return fundTranType;
        }

        public void setFundTranType(String fundTranType) {
            this.fundTranType = fundTranType;
        }

        public String getBonusType() {
            return bonusType;
        }

        public void setBonusType(String bonusType) {
            this.bonusType = bonusType;
        }

        public String getTransStatus() {
            return transStatus;
        }

        public void setTransStatus(String transStatus) {
            this.transStatus = transStatus;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getSpecialTransFlag() {
            return specialTransFlag;
        }

        public void setSpecialTransFlag(String specialTransFlag) {
            this.specialTransFlag = specialTransFlag;
        }

        public String getFundRegCode() {
            return fundRegCode;
        }

        public void setFundRegCode(String fundRegCode) {
            this.fundRegCode = fundRegCode;
        }

        public String getFundRegName() {
            return fundRegName;
        }

        public void setFundRegName(String fundRegName) {
            this.fundRegName = fundRegName;
        }

        public String getTaAccountNo() {
            return taAccountNo;
        }

        public void setTaAccountNo(String taAccountNo) {
            this.taAccountNo = taAccountNo;
        }

        public String getConfirmDate() {
            return confirmDate;
        }

        public void setConfirmDate(String confirmDate) {
            this.confirmDate = confirmDate;
        }

        public String getInFundCode() {
            return inFundCode;
        }

        public void setInFundCode(String inFundCode) {
            this.inFundCode = inFundCode;
        }

        public String getInFundName() {
            return inFundName;
        }

        public void setInFundName(String inFundName) {
            this.inFundName = inFundName;
        }

        public String getCancleFlag() {
            return cancleFlag;
        }

        public void setCancleFlag(String cancleFlag) {
            this.cancleFlag = cancleFlag;
        }

        public String getOrignFundSeq() {
            return orignFundSeq;
        }

        public void setOrignFundSeq(String orignFundSeq) {
            this.orignFundSeq = orignFundSeq;
        }
    }
}
