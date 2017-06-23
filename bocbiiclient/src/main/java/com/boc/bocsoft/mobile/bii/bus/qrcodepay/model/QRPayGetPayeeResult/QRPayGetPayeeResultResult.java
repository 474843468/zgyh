package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult;

import java.util.List;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRPayGetPayeeResultResult {

    /**
     * id : 3
     * method : QRPayGetTransRecord
     * recordNumber : 2
     * result : {"List":[{"tranSeq":"fweuioru2333","tranTime":"2016/02/03","tranStatus":"0","tranAmount":"125.32","tranRemark":null},{"tranSeq":"fweuioru2334","tranTime":"2016/02/03","tranStatus":"1","transAmount":"125.32","tranRemark":"1"}]}
     * error : null
     */

    private String recordNumber;

    private List<QRPayGetPayeeResultResult.ResultBean> mResultBeanList;


    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ResultBean> getResultBeanList() {
        return mResultBeanList;
    }

    public void setResultBeanList(List<ResultBean> resultBeanList) {
        mResultBeanList = resultBeanList;
    }

    public static class ResultBean {
        /**
         * tranSeq : fweuioru2333
         * tranTime : 2016/02/03
         * tranStatus : 0
         * tranAmount : 125.32
         * tranRemark : null
         */
        private String tranStatus;
        private String amount;
        private String currencyCode;
        private String accNo;
        private String accName;
        private String voucherNum;
        private String payerComments;

        public String getTranStatus() {
            return tranStatus;
        }

        public void setTranStatus(String tranStatus) {
            this.tranStatus = tranStatus;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }

        public String getAccName() {
            return accName;
        }

        public void setAccName(String accName) {
            this.accName = accName;
        }

        public String getVoucherNum() {
            return voucherNum;
        }

        public void setVoucherNum(String voucherNum) {
            this.voucherNum = voucherNum;
        }

        public String getPayerComments() {
            return payerComments;
        }

        public void setPayerComments(String payerComments) {
            this.payerComments = payerComments;
        }
    }
}
