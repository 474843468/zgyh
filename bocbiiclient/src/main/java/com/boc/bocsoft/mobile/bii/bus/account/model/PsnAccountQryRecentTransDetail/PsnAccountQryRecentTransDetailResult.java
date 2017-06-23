package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/8/16 18:31
 *         最新交易明细新接口
 */
public class PsnAccountQryRecentTransDetailResult {

    /** 记录数 */
    private int recordNumber;
    /** 交易明细 */
    private List<TransDetail> List;

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<TransDetail> getTransDetails() {
        return List;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setTransDetails(List<TransDetail> transDetails) {
        this.List = transDetails;
    }

    public static class TransDetail {

        /** 币种 */
        private String currency;
        /** 余额 */
        private BigDecimal balance;
        /** 收入或者支出金额  -- 带负号表示支出不带负号表示收入 */
        private BigDecimal amount;
        /** 交易日期 */
        private LocalDate paymentDate;
        /** 钞/汇  01 = 现钞, 02 = 现汇*/
        private String cashRemit;
        /** 冲正标识 true 是,false 否*/
        private boolean chargeBack;
        /** 业务摘要 */
        private String businessDigest;
        /** 附言 */
        private String furInfo;
        /** 对方账户明细 */
        private String payeeAccountName;
        /** 对方账户账号 */
        private String payeeAccountNumber;
        /** 交易渠道 */
        private String transChnl;
        /** 网点或终端信息 */
        private String chnlDetail;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
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
