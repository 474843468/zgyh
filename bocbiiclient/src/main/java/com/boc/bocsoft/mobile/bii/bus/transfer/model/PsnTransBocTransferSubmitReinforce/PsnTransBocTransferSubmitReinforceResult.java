package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmitReinforce;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnTransBocTransferSubmitReinforceResult {

    /**
     * method : PsnTransBocTransferSubmitReinforce
     * id : 6
     * status : 0
     * result : {"status":"7","currency":"001","transactionId":2414187,"amount":3355,"batSeq":41032692,"commissionCharge":null,"postage":null,"fromAccountNum":"456321342443","fromAccountNickname":"借记卡","fromAccountType":"119","fromIbkNum":"43016","needCount":null}
     * error : null
     */

        private ResultBean result;
        private Object error;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public static class ResultBean {
            private String status;
            private String currency;
            private int transactionId;
            private int amount;
            private int batSeq;
            private Object commissionCharge;
            private Object postage;
            private String fromAccountNum;
            private String fromAccountNickname;
            private String fromAccountType;
            private String fromIbkNum;
            private Object needCount;

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

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
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

            public String getFromIbkNum() {
                return fromIbkNum;
            }

            public void setFromIbkNum(String fromIbkNum) {
                this.fromIbkNum = fromIbkNum;
            }

            public Object getNeedCount() {
                return needCount;
            }

            public void setNeedCount(Object needCount) {
                this.needCount = needCount;
            }
        }
}
