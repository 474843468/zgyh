package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail;

import java.util.List;

/**
 * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryBilledTransDetailResult {
    private String sumNo;
    private String pageNo;
    private String primary;
    private String dealCount;
    private List<TransListBean> transList;

    public String getSumNo() {
        return sumNo;
    }

    public void setSumNo(String sumNo) {
        this.sumNo = sumNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getDealCount() {
        return dealCount;
    }

    public void setDealCount(String dealCount) {
        this.dealCount = dealCount;
    }

    public List<TransListBean> getTransList() {
        return transList;
    }

    public void setTransList(List<TransListBean> transList) {
        this.transList = transList;
    }

    public static class TransListBean {
        private String acntType;
        private String dealDt;
        private String checkDt;
        private String dealCardId;
        private String dealDesc;
        private String dealCcy;
        private String transactionProfileCode;
        private String dealCnt;
        private String balCnt;
        private String loanSign;

        public String getAcntType() {
            return acntType;
        }

        public void setAcntType(String acntType) {
            this.acntType = acntType;
        }

        public String getDealDt() {
            return dealDt;
        }

        public void setDealDt(String dealDt) {
            this.dealDt = dealDt;
        }

        public String getCheckDt() {
            return checkDt;
        }

        public void setCheckDt(String checkDt) {
            this.checkDt = checkDt;
        }

        public String getDealCardId() {
            return dealCardId;
        }

        public void setDealCardId(String dealCardId) {
            this.dealCardId = dealCardId;
        }

        public String getDealDesc() {
            return dealDesc;
        }

        public void setDealDesc(String dealDesc) {
            this.dealDesc = dealDesc;
        }

        public String getDealCcy() {
            return dealCcy;
        }

        public void setDealCcy(String dealCcy) {
            this.dealCcy = dealCcy;
        }

        public String getTransactionProfileCode() {
            return transactionProfileCode;
        }

        public void setTransactionProfileCode(String transactionProfileCode) {
            this.transactionProfileCode = transactionProfileCode;
        }

        public String getDealCnt() {
            return dealCnt;
        }

        public void setDealCnt(String dealCnt) {
            this.dealCnt = dealCnt;
        }

        public String getBalCnt() {
            return balCnt;
        }

        public void setBalCnt(String balCnt) {
            this.balCnt = balCnt;
        }

        public String getLoanSign() {
            return loanSign;
        }

        public void setLoanSign(String loanSign) {
            this.loanSign = loanSign;
        }
    }
}
