package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by WY on 2016/6/12.
 */
public class PsnTransLinkTransferSubmitResult {

    /**
     * method : PsnTransLinkTransferSubmit

         * currency : 001
         * status : 7
         * transactionId : null
         * amount : 200
         * batSeq : 41032708
         * commissionCharge : null
         * furInfo : 无备注
         * postage : null
         * cashRemit : null
         * fromAccountType : 119
         * fromAccountNumber : 4563510100889932269
         * fromAccountNickname : 借记卡电子签名
         * fromAccountIbknum : 40740
         * toAccountType : 119
         * toAccountNumber : 45635101008899324623
         * toAccountNickname : 长城电子借记卡
         * toAccountIbknum : 40740
         * needCount : null
         */
            private String currency;
            private String status;
            private Long  transactionId;
            private BigDecimal amount;
            private Long batSeq;
            private BigDecimal commissionCharge;
            private String furInfo;
            private BigDecimal postage;
            private BigDecimal finalCommissionCharge;
            private String cashRemit;
            private String fromAccountType;
            private String fromAccountNumber;
            private String fromAccountNickname;
            private String fromAccountIbknum;
            private String toAccountType;
            private String toAccountNumber;
            private String toAccountNickname;
            private String toAccountIbknum;
            private int needCount;

    public BigDecimal getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(BigDecimal commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public int getNeedCount() {
        return needCount;
    }

    public void setNeedCount(int needCount) {
        this.needCount = needCount;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Long getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(Long transactionId) {
                this.transactionId = transactionId;
            }

            public BigDecimal getAmount() {
                return amount;
            }

            public void setAmount(BigDecimal amount) {
                this.amount = amount;
            }

            public Long getBatSeq() {
                return batSeq;
            }

            public void setBatSeq(Long batSeq) {
                this.batSeq = batSeq;
            }


            public String getFurInfo() {
                return furInfo;
            }

            public void setFurInfo(String furInfo) {
                this.furInfo = furInfo;
            }


            public String getCashRemit() {
                return cashRemit;
            }

            public void setCashRemit(String cashRemit) {
                this.cashRemit = cashRemit;
            }

            public String getFromAccountType() {
                return fromAccountType;
            }

            public void setFromAccountType(String fromAccountType) {
                this.fromAccountType = fromAccountType;
            }

            public String getFromAccountNumber() {
                return fromAccountNumber;
            }

            public void setFromAccountNumber(String fromAccountNumber) {
                this.fromAccountNumber = fromAccountNumber;
            }

            public String getFromAccountNickname() {
                return fromAccountNickname;
            }

            public void setFromAccountNickname(String fromAccountNickname) {
                this.fromAccountNickname = fromAccountNickname;
            }

            public String getFromAccountIbknum() {
                return fromAccountIbknum;
            }

            public void setFromAccountIbknum(String fromAccountIbknum) {
                this.fromAccountIbknum = fromAccountIbknum;
            }

            public String getToAccountType() {
                return toAccountType;
            }

            public void setToAccountType(String toAccountType) {
                this.toAccountType = toAccountType;
            }

            public String getToAccountNumber() {
                return toAccountNumber;
            }

            public void setToAccountNumber(String toAccountNumber) {
                this.toAccountNumber = toAccountNumber;
            }

            public String getToAccountNickname() {
                return toAccountNickname;
            }

            public void setToAccountNickname(String toAccountNickname) {
                this.toAccountNickname = toAccountNickname;
            }

            public String getToAccountIbknum() {
                return toAccountIbknum;
            }

            public void setToAccountIbknum(String toAccountIbknum) {
                this.toAccountIbknum = toAccountIbknum;
            }



}

