package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmitReinforce;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnDirTransBocTransferSubmitReinforceResult {

    /**
     * method : PsnDirTransBocTransferSubmitReinforce
     * id : 2
     * status : 01
     * result : {"status":"7","currency":"001","transactionId":3143183,"amount":0.12,"batSeq":41136189,"commissionCharge":null,"postage":null,"fromAccountNum":"100000147954","fromAccountNickname":"新线河北借记卡测试","fromAccountType":"119","needCount":null,"fromIbkNum":"42465"}
     * error : null
     */

            private String status;
            private String currency;
            private int transactionId;
            private double amount;
            private int batSeq;
            private Object commissionCharge;
            private Object postage;
            private String fromAccountNum;
            private String fromAccountNickname;
            private String fromAccountType;
            private Object needCount;
            private String fromIbkNum;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public int getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(int transactionId) {
                this.transactionId = transactionId;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getBatSeq() {
                return batSeq;
            }

            public void setBatSeq(int batSeq) {
                this.batSeq = batSeq;
            }

            public Object getCommissionCharge() {
                return commissionCharge;
            }

            public void setCommissionCharge(Object commissionCharge) {
                this.commissionCharge = commissionCharge;
            }

            public Object getPostage() {
                return postage;
            }

            public void setPostage(Object postage) {
                this.postage = postage;
            }

            public String getFromAccountNum() {
                return fromAccountNum;
            }

            public void setFromAccountNum(String fromAccountNum) {
                this.fromAccountNum = fromAccountNum;
            }

            public String getFromAccountNickname() {
                return fromAccountNickname;
            }

            public void setFromAccountNickname(String fromAccountNickname) {
                this.fromAccountNickname = fromAccountNickname;
            }

            public String getFromAccountType() {
                return fromAccountType;
            }

            public void setFromAccountType(String fromAccountType) {
                this.fromAccountType = fromAccountType;
            }

            public Object getNeedCount() {
                return needCount;
            }

            public void setNeedCount(Object needCount) {
                this.needCount = needCount;
            }

            public String getFromIbkNum() {
                return fromIbkNum;
            }

            public void setFromIbkNum(String fromIbkNum) {
                this.fromIbkNum = fromIbkNum;
            }
}
