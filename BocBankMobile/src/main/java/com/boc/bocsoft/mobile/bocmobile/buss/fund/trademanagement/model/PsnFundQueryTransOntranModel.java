package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model;

import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy7105 on 2016/11/25.
 * 在途交易列表model
 */
public class PsnFundQueryTransOntranModel {
    //请求上报数据
    /**
     * currentIndex : 0
     * pageSize : 15
     * _refresh : false
     */

    private String currentIndex;
    private String pageSize;
    private String _refresh;
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    //请求返回数据
    /**
     * recordNumber : 4
     * list : [{"currencyCode":null,"paymentDate":"2013/08/30","transAmount":"22222.22","fundCode":"123456","fundName":"12345678901234567890","appointDate":"2013/08/30","transCount":"3333.30","fundTranType":"123","bonusType":"1","transStatus":"1234567890","cashFlag":"CAS","specialTransFlag":"0","fundRegCode":null,"fundRegName":null,"taAccountNo":null,"confirmDate":null,"inFundCode":"123456","inFundName":"1234567890","cancleFlag":"Y","orignFundSeq":"123456789012"}]
     */

    private int recordNumber;
    /**
     * currencyCode : null
     * paymentDate : 2013/08/30
     * transAmount : 22222.22
     * fundCode : 123456
     * fundName : 12345678901234567890
     * appointDate : 2013/08/30
     * transCount : 3333.30
     * fundTranType : 123
     * bonusType : 1
     * transStatus : 1234567890
     * cashFlag : CAS
     * specialTransFlag : 0
     * fundRegCode : null
     * fundRegName : null
     * taAccountNo : null
     * confirmDate : null
     * inFundCode : 123456
     * inFundName : 1234567890
     * cancleFlag : Y
     * orignFundSeq : 123456789012
     */

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<PsnFundQueryTransOntranModel.ListBean> getList() {
        return list;
    }

    public void setList(List<PsnFundQueryTransOntranModel.ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        private String currencyCode;
        private String paymentDate;//委托日期
        private String transAmount;
        private String fundCode;
        private String fundName;//基金名称
        private String appointDate;
        private String transCount;
        private String fundTranType;//交易类型
        private String bonusType;
        private String transStatus;//交易状态
        private String cashFlag;
        private String specialTransFlag;
        private String fundRegCode;
        private String fundRegName;
        private String taAccountNo;
        private String confirmDate;
        private String inFundCode;
        private String inFundName;
        private String cancleFlag;
        private String orignFundSeq;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public LocalDate getPaymentDate() {
            LocalDate localDate = LocalDate.parse(paymentDate, DateFormatters.dateFormatter1);
            return localDate;
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

        public LocalDate getAppointDate() {
            LocalDate localDate = LocalDate.parse(appointDate, DateFormatters.dateFormatter1);
            return localDate;
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

        public LocalDate getConfirmDate() {
            return LocalDate.parse(confirmDate, DateFormatters.dateFormatter1);
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

    //
//    public static PsnFundConsignAbortParams generateFundConsignAbortParams(PsnFundQueryTransOntranModel model,String token,String conversationId){
//        PsnFundConsignAbortParams params = new PsnFundConsignAbortParams();
//        params.setFundCode(model.);
//        return params;
//    }
}
