package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery;

import java.util.List;

/**
 * 产品查询与购买响应（登录后）
 * Created by liuweidong on 2016/9/13.
 */
public class PsnXpadProductListQueryResult {

    private String recordNumber;

    private List<ListBean> list;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String sellingEndingDate;
        private String subPayAmt;
        private String impawnPermit;
        private String remainCycleCount;
        private String sellingStartingDate;
        private String dayIncreaseRate;
        private String rateDetail;// 预计年收益率（%）(最大值)
        private String periedTime;
        private String curCode;
        private String isCancel;
        private String isLockPeriod;
        private String progressionflag;
        private String price;// 单位净值
        private String isAgreement;
        private String prodCode;// 产品代码
        private String isBuy;
        private String periodical;
        private String priceDate;
        private String prodName;
        private String issueType;
        private String termType;
        private String autoPermit;
        private String yearlyRR;
        private String status;
        private String prodRisklvl;
        private String availableAmt;
        private String isProfitest;
        private String accumulatePrice;
        private String productKind;

        public String getSellingEndingDate() {
            return sellingEndingDate;
        }

        public void setSellingEndingDate(String sellingEndingDate) {
            this.sellingEndingDate = sellingEndingDate;
        }

        public String getSubPayAmt() {
            return subPayAmt;
        }

        public void setSubPayAmt(String subPayAmt) {
            this.subPayAmt = subPayAmt;
        }

        public String getImpawnPermit() {
            return impawnPermit;
        }

        public void setImpawnPermit(String impawnPermit) {
            this.impawnPermit = impawnPermit;
        }

        public String getRemainCycleCount() {
            return remainCycleCount;
        }

        public void setRemainCycleCount(String remainCycleCount) {
            this.remainCycleCount = remainCycleCount;
        }

        public String getSellingStartingDate() {
            return sellingStartingDate;
        }

        public void setSellingStartingDate(String sellingStartingDate) {
            this.sellingStartingDate = sellingStartingDate;
        }

        public String getDayIncreaseRate() {
            return dayIncreaseRate;
        }

        public void setDayIncreaseRate(String dayIncreaseRate) {
            this.dayIncreaseRate = dayIncreaseRate;
        }

        public String getRateDetail() {
            return rateDetail;
        }

        public void setRateDetail(String rateDetail) {
            this.rateDetail = rateDetail;
        }

        public String getPeriedTime() {
            return periedTime;
        }

        public void setPeriedTime(String periedTime) {
            this.periedTime = periedTime;
        }

        public String getCurCode() {
            return curCode;
        }

        public void setCurCode(String curCode) {
            this.curCode = curCode;
        }

        public String getIsCancel() {
            return isCancel;
        }

        public void setIsCancel(String isCancel) {
            this.isCancel = isCancel;
        }

        public String getIsLockPeriod() {
            return isLockPeriod;
        }

        public void setIsLockPeriod(String isLockPeriod) {
            this.isLockPeriod = isLockPeriod;
        }

        public String getProgressionflag() {
            return progressionflag;
        }

        public void setProgressionflag(String progressionflag) {
            this.progressionflag = progressionflag;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIsAgreement() {
            return isAgreement;
        }

        public void setIsAgreement(String isAgreement) {
            this.isAgreement = isAgreement;
        }

        public String getProdCode() {
            return prodCode;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getIsBuy() {
            return isBuy;
        }

        public void setIsBuy(String isBuy) {
            this.isBuy = isBuy;
        }

        public String getPeriodical() {
            return periodical;
        }

        public void setPeriodical(String periodical) {
            this.periodical = periodical;
        }

        public String getPriceDate() {
            return priceDate;
        }

        public void setPriceDate(String priceDate) {
            this.priceDate = priceDate;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getIssueType() {
            return issueType;
        }

        public void setIssueType(String issueType) {
            this.issueType = issueType;
        }

        public String getTermType() {
            return termType;
        }

        public void setTermType(String termType) {
            this.termType = termType;
        }

        public String getAutoPermit() {
            return autoPermit;
        }

        public void setAutoPermit(String autoPermit) {
            this.autoPermit = autoPermit;
        }

        public String getYearlyRR() {
            return yearlyRR;
        }

        public void setYearlyRR(String yearlyRR) {
            this.yearlyRR = yearlyRR;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProdRisklvl() {
            return prodRisklvl;
        }

        public void setProdRisklvl(String prodRisklvl) {
            this.prodRisklvl = prodRisklvl;
        }

        public String getAvailableAmt() {
            return availableAmt;
        }

        public void setAvailableAmt(String availableAmt) {
            this.availableAmt = availableAmt;
        }

        public String getIsProfitest() {
            return isProfitest;
        }

        public void setIsProfitest(String isProfitest) {
            this.isProfitest = isProfitest;
        }

        public String getAccumulatePrice() {
            return accumulatePrice;
        }

        public void setAccumulatePrice(String accumulatePrice) {
            this.accumulatePrice = accumulatePrice;
        }

        public String getProductKind() {
            return productKind;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

    }
}
