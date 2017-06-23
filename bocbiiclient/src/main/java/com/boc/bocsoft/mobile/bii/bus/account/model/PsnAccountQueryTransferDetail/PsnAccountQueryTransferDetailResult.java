package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Created by wangf on 2016/6/4.
 */
public class PsnAccountQueryTransferDetailResult {

    /**
     * List : [{"chargeBack":false,"paymentDate":"2018/12/11","currency":"001","cashRemit":"01","amount":-50000,"balance":829089.39,"businessDigest":"交易成功","furInfo":"你好","payeeAccountName":"北京丰台","payeeAccountNumber":"6225550129931204","transChnl":"交易渠道","chnlDetail":""},{"chargeBack":false,"paymentDate":"2018/12/11","currency":"001","cashRemit":"01","amount":-50000,"balance":829089.39,"businessDigest":"交易成功","furInfo":"你好","payeeAccountName":"北京丰台","payeeAccountNumber":"6225550129931204","transChnl":"交易渠道","chnlDetail":"网点或终端信息"},{"chargeBack":false,"paymentDate":"2018/12/11","currency":"001","cashRemit":"01","amount":-50000,"balance":829089.39,"businessDigest":"交易成功","furInfo":"你好","payeeAccountName":"北京丰台","payeeAccountNumber":"6225550129931204","transChnl":"交易渠道","chnlDetail":"网点或终端信息"}]
     * recordNumber : 55
     */

    /**
     * 总记录数
     */
    private int recordNumber;

    /**
     * chargeBack : false
     * paymentDate : 2018/12/11
     * currency : 001
     * cashRemit : 01
     * amount : -50000
     * balance : 829089.39
     * businessDigest : 交易成功
     * furInfo : 你好
     * payeeAccountName : 北京丰台
     * payeeAccountNumber : 6225550129931204
     * transChnl : 交易渠道
     * chnlDetail :
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
         * 冲正标识
         */
        private boolean chargeBack;
        /**
         * 交易日期
         */
        private LocalDate paymentDate;
        /**
         * 交易币种
         * 001 = 人民币
         * 014 = 美元
         * 027 = 日元
         * 038 = 欧元
         */
        private String currency;
        /**
         * 钞汇
         * 01 = 现钞
         * 02 = 现汇
         */
        private String cashRemit;
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
         * 交易渠道
         * 后台直接返回汉字
         */
        private String transChnl;
        /**
         * 网店或终端信息
         * 后台直接返回汉字
         */
        private String chnlDetail;

        public boolean isChargeBack() {
            return chargeBack;
        }

        public void setChargeBack(boolean chargeBack) {
            this.chargeBack = chargeBack;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

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
