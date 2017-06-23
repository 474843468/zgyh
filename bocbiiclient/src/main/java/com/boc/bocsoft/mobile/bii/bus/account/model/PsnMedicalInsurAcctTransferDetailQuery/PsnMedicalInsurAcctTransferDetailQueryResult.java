package com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 医保账户交易明细查询返回报文
 * Created by niuguobin on 2016/6/23.
 */
public class PsnMedicalInsurAcctTransferDetailQueryResult {

    /**
     * List : [{"amount":-0.08,"balance":909.55,"businessDigest":"结利息税","cashRemit":"","chargeBack":false,"currency":"001","furInfo":"","payeeAccountName":"","payeeAccountNumber":"","paymentDate":"2016/09/20"},{"amount":1.52,"balance":909.63,"businessDigest":"结息","cashRemit":"","chargeBack":false,"currency":"001","furInfo":"","payeeAccountName":"","payeeAccountNumber":"","paymentDate":"2016/09/20"},{"amount":-147100,"balance":147100,"businessDigest":"贷款放款","cashRemit":"","chargeBack":false,"currency":"001","furInfo":"","payeeAccountName":"","payeeAccountNumber":"","paymentDate":"2016/09/20"}]
     * recordNumber : 3
     */

    /**
     * 总记录数
     */
    private int recordNumber;
    /**
     * amount : -0.08
     * balance : 909.55
     * businessDigest : 结利息税
     * cashRemit :
     * chargeBack : false
     * currency : 001
     * furInfo :
     * payeeAccountName :
     * payeeAccountNumber :
     * paymentDate : 2016/09/20
     */

    private java.util.List<ListBean> List;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {

        /**
         * 收入或支出金额
         */
        private String amount;
        /**
         * 余额
         */
        private String balance;
        /**
         * 业务摘要
         */
        private String businessDigest;
        /**
         * 钞汇
         */
        private String cashRemit;
        /**
         * 冲正标识
         */
        private boolean chargeBack;
        /**
         * 交易货币
         */
        private String currency;
        /**
         * 附言
         */
        private String furInfo;
        /**
         * 对方账户名称
         */
        private String payeeAccountName;
        /**
         * 对方账户账号
         */
        private String payeeAccountNumber;
        /**
         * 交易日期
         */
        private LocalDate paymentDate;
        /**
         * 交易渠道
         */
        private String transChnl;
        /**
         * 网点或终端信息
         */
        private String chnlDetail;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBusinessDigest() {
            return businessDigest;
        }

        public void setBusinessDigest(String businessDigest) {
            this.businessDigest = businessDigest;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public boolean isChargeBack() {
            return chargeBack;
        }

        public void setChargeBack(boolean chargeBack) {
            this.chargeBack = chargeBack;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getFurInfo() {
            return furInfo;
        }

        public void setFurInfo(String furInfo) {
            this.furInfo = furInfo;
        }

        public String getPayeeAccountName() {
            return payeeAccountName;
        }

        public void setPayeeAccountName(String payeeAccountName) {
            this.payeeAccountName = payeeAccountName;
        }

        public String getPayeeAccountNumber() {
            return payeeAccountNumber;
        }

        public void setPayeeAccountNumber(String payeeAccountNumber) {
            this.payeeAccountNumber = payeeAccountNumber;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getTransChnl() {
            return transChnl;
        }

        public void setTransChnl(String transChnl) {
            this.transChnl = transChnl;
        }

        public String getChnlDetail() {
            return chnlDetail;
        }

        public void setChnlDetail(String chnlDetail) {
            this.chnlDetail = chnlDetail;
        }
    }
}
