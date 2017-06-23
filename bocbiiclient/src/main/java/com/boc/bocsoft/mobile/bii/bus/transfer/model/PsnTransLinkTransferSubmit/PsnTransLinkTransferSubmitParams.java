package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit;

import java.security.PrivateKey;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnTransLinkTransferSubmitParams  {

    /**
     * method : PsnTransLinkTransferSubmit
     * params : {"dueDate":"","devicePrint":"","payeeActno":"456456789012346","payeeName":"孙佳乐","fromAccountId":"101240297","toAccountId":"101249306","currency":"001","amount":"2","cashRemit":"-","remark":"备注","executeType":"0","executeDate":"","startDate":"","endDate":"","cycleSelect":"","token":"v8w2gstn"}
     */

        private String dueDate;
        private String devicePrint;
        private String payeeActno;
        private String payeeName;
        private String fromAccountId;
        private String toAccountId;
        private String currency;
        private String amount;
        private String cashRemit;
        private String remark;
        private String executeType;
        private String executeDate;
        private String startDate;
        private String endDate;
        private String cycleSelect;
        private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;
    private String toAccountType;
    private String payeeBankNum;
    private String payeeId;
    private String _signedData;
    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String signedData) {
        this._signedData = signedData;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }







        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getDevicePrint() {
            return devicePrint;
        }

        public void setDevicePrint(String devicePrint) {
            this.devicePrint = devicePrint;
        }

        public String getPayeeActno() {
            return payeeActno;
        }

        public void setPayeeActno(String payeeActno) {
            this.payeeActno = payeeActno;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getFromAccountId() {
            return fromAccountId;
        }

        public void setFromAccountId(String fromAccountId) {
            this.fromAccountId = fromAccountId;
        }

        public String getToAccountId() {
            return toAccountId;
        }

        public void setToAccountId(String toAccountId) {
            this.toAccountId = toAccountId;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getExecuteType() {
            return executeType;
        }

        public void setExecuteType(String executeType) {
            this.executeType = executeType;
        }

        public String getExecuteDate() {
            return executeDate;
        }

        public void setExecuteDate(String executeDate) {
            this.executeDate = executeDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCycleSelect() {
            return cycleSelect;
        }

        public void setCycleSelect(String cycleSelect) {
            this.cycleSelect = cycleSelect;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

}
