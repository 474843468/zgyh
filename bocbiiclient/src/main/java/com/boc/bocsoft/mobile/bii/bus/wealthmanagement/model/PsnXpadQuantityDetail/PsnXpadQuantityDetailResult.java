package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * 4.68 068份额明细查询  PsnXpadQuantityDetail
 * Created by zn on 2016/9/27.
 */
public class PsnXpadQuantityDetailResult {
    /**
     * 份额总笔数	String
     */
    private String recordNumber;

    private List<ListEntity> list;

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String sellPrice;

        private String prodCode;

        private String canChangeBonusMode;
        private String currentBonusMode;

        private String progressionflag;

        private String productKind;

        private String price;
        private String priceDate;
        private String expAmt;
        private String termType;
        private String canAddBuy;
        private String standardPro;

        private String canAssignDate;
        private String shareValue;

        private String yearlyRRMax;

        private String issueType;

        public String getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getProdCode() {
            return prodCode;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getCanChangeBonusMode() {
            return canChangeBonusMode;
        }

        public void setCanChangeBonusMode(String canChangeBonusMode) {
            this.canChangeBonusMode = canChangeBonusMode;
        }

        public String getCurrentBonusMode() {
            return currentBonusMode;
        }

        public void setCurrentBonusMode(String currentBonusMode) {
            this.currentBonusMode = currentBonusMode;
        }

        public String getProgressionflag() {
            return progressionflag;
        }

        public void setProgressionflag(String progressionflag) {
            this.progressionflag = progressionflag;
        }

        public String getProductKind() {
            return productKind;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceDate() {
            return priceDate;
        }

        public void setPriceDate(String priceDate) {
            this.priceDate = priceDate;
        }

        public String getExpAmt() {
            return expAmt;
        }

        public void setExpAmt(String expAmt) {
            this.expAmt = expAmt;
        }

        public String getTermType() {
            return termType;
        }

        public void setTermType(String termType) {
            this.termType = termType;
        }

        public String getCanAddBuy() {
            return canAddBuy;
        }

        public void setCanAddBuy(String canAddBuy) {
            this.canAddBuy = canAddBuy;
        }

        public String getStandardPro() {
            return standardPro;
        }

        public void setStandardPro(String standardPro) {
            this.standardPro = standardPro;
        }

        public String getCanAssignDate() {
            return canAssignDate;
        }

        public void setCanAssignDate(String canAssignDate) {
            this.canAssignDate = canAssignDate;
        }

        public String getShareValue() {
            return shareValue;
        }

        public void setShareValue(String shareValue) {
            this.shareValue = shareValue;
        }

        public String getYearlyRRMax() {
            return yearlyRRMax;
        }

        public void setYearlyRRMax(String yearlyRRMax) {
            this.yearlyRRMax = yearlyRRMax;
        }

        public String getIssueType() {
            return issueType;
        }

        public void setIssueType(String issueType) {
            this.issueType = issueType;
        }

        /**
         * 客户理财账户	String	加星返回
         */
        private String xpadAccount;
        /**
         * 银行账号	String	加星返回
         */
        private String bancAccount;
        /**
         * 产品名称	String
         */
        private String prodName;
        /**
         * 产品币种	String	001：人民币元
         * 014：美元
         * 012：英镑
         * 013：港币
         * 028: 加拿大元
         * 029：澳元
         * 038：欧元
         * 027：日元
         */
        private String curCode;
        /**
         * 预计年收益率（%）-	String	收益累进、与时聚金产品展示区间
         */
        private String yearlyRR;
        /**
         * 产品到期日	String	无限开放式产品返回“-1”表示“无限期”
         */
        private String prodEnd;
        /**
         * 持有份额	String
         */
        private String holdingQuantity;
        /**
         * 可用份额	String
         */
        private String availableQuantity;
        /**
         * 是否可赎回	String	0：是
         * 1：否
         */
        private String canRedeem;
        /**
         * 是否允许部分赎回	String	0：是
         * 1：否
         */
        private String canPartlyRedeem;
        /**
         * 最低持有份额	String
         */
        private String lowestHoldQuantity;
        /**
         * 赎回起点金额	String
         */
        private String redeemStartingAmount;
        /**
         * 钞汇标识	String	01：钞
         * 02：汇
         * 00：人民币
         */
        private String cashRemit;
        /**
         * 参考收益-	String
         */
        private String expProfit;
        /**
         * 是否可份额转换	String	业绩基准类产品有效
         * 0：是
         * 1：否
         */
        private String canQuantityExchange;
        /**
         * 产品期限-	String	固定期限类产品有效，-1代表无限期
         */
        private String productTerm;
        /**
         * 是否可投资协议管理	String	0：不允许
         * 1：允许
         */
        private String canAgreementMange;
        /**
         * 当前期数	String	产品当前期数，业绩基准-周期滚续产品有效
         */
        private String currPeriod;
        /**
         * 总期数	String	总期数，业绩基准-周期滚续产品有效
         */
        private String totalPeriod;
        /**
         * 产品起息日	String
         */
        private String prodBegin;
        /**
         * 交易流水号	String
         */
        private String tranSeq;
        /**
         * 资金账号缓存标识	String	用于参考收益等交易的上送
         */
        private String bancAccountKey;

        public String getXpadAccount() {
            return xpadAccount;
        }

        public void setXpadAccount(String xpadAccount) {
            this.xpadAccount = xpadAccount;
        }

        public String getBancAccount() {
            return bancAccount;
        }

        public void setBancAccount(String bancAccount) {
            this.bancAccount = bancAccount;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getCurCode() {
            return curCode;
        }

        public void setCurCode(String curCode) {
            this.curCode = curCode;
        }

        public String getYearlyRR() {
            return yearlyRR;
        }

        public void setYearlyRR(String yearlyRR) {
            this.yearlyRR = yearlyRR;
        }

        public String getProdEnd() {
            return prodEnd;
        }

        public void setProdEnd(String prodEnd) {
            this.prodEnd = prodEnd;
        }

        public String getHoldingQuantity() {
            return holdingQuantity;
        }

        public void setHoldingQuantity(String holdingQuantity) {
            this.holdingQuantity = holdingQuantity;
        }

        public String getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(String availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public String getCanRedeem() {
            return canRedeem;
        }

        public void setCanRedeem(String canRedeem) {
            this.canRedeem = canRedeem;
        }

        public String getCanPartlyRedeem() {
            return canPartlyRedeem;
        }

        public void setCanPartlyRedeem(String canPartlyRedeem) {
            this.canPartlyRedeem = canPartlyRedeem;
        }

        public String getLowestHoldQuantity() {
            return lowestHoldQuantity;
        }

        public void setLowestHoldQuantity(String lowestHoldQuantity) {
            this.lowestHoldQuantity = lowestHoldQuantity;
        }

        public String getRedeemStartingAmount() {
            return redeemStartingAmount;
        }

        public void setRedeemStartingAmount(String redeemStartingAmount) {
            this.redeemStartingAmount = redeemStartingAmount;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getExpProfit() {
            return expProfit;
        }

        public void setExpProfit(String expProfit) {
            this.expProfit = expProfit;
        }

        public String getCanQuantityExchange() {
            return canQuantityExchange;
        }

        public void setCanQuantityExchange(String canQuantityExchange) {
            this.canQuantityExchange = canQuantityExchange;
        }

        public String getProductTerm() {
            return productTerm;
        }

        public void setProductTerm(String productTerm) {
            this.productTerm = productTerm;
        }

        public String getCanAgreementMange() {
            return canAgreementMange;
        }

        public void setCanAgreementMange(String canAgreementMange) {
            this.canAgreementMange = canAgreementMange;
        }

        public String getCurrPeriod() {
            return currPeriod;
        }

        public void setCurrPeriod(String currPeriod) {
            this.currPeriod = currPeriod;
        }

        public String getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(String totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public String getProdBegin() {
            return prodBegin;
        }

        public void setProdBegin(String prodBegin) {
            this.prodBegin = prodBegin;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public String getBancAccountKey() {
            return bancAccountKey;
        }

        public void setBancAccountKey(String bancAccountKey) {
            this.bancAccountKey = bancAccountKey;
        }
    }


}
