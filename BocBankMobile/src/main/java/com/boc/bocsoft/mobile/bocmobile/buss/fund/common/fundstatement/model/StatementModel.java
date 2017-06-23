package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huixioabo on 2016/11/23.
 * 基金持仓信息页面数据
 */
public class StatementModel {

    private String cardNo;
    private String certificateCode;
    private String certificateId;
    private String count;
    private String cusName;
    private String endflag;
    private String fundAccount;
    private String seqnum;
    /**
     * availableBalance : 120000.99
     * bonusType : 1
     * cashFlag : 2
     * currency : 001
     * floatLoss : 130.12
     * freezeBlance : 20000
     * fundCode : 001356
     * fundName : 中银保险
     * marketValue : 1200
     * netPrice : 1.0
     * netPriceDate : 20131201
     * totalBalance : 1000000
     */

    private List<ListBean> list;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getEndflag() {
        return endflag;
    }

    public void setEndflag(String endflag) {
        this.endflag = endflag;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getSeqnum() {
        return seqnum;
    }

    public void setSeqnum(String seqnum) {
        this.seqnum = seqnum;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PsnFundStatementBalanceQueryResult{" +
                "cardNo='" + cardNo + '\'' +
                ", certificateCode='" + certificateCode + '\'' +
                ", certificateId='" + certificateId + '\'' +
                ", count='" + count + '\'' +
                ", cusName='" + cusName + '\'' +
                ", endflag='" + endflag + '\'' +
                ", fundAccount='" + fundAccount + '\'' +
                ", seqnum='" + seqnum + '\'' +
                ", list=" + list +
                '}';
    }

    public static class ListBean implements Serializable {
        private String availableBalance;
        private String bonusType;
        private String cashFlag;
        private String currency;
        private BigDecimal floatLoss;
        private String freezeBlance;
        private String fundCode;
        private String fundName;
        private BigDecimal marketValue;
        private String netPrice;
        private String netPriceDate;
        private String totalBalance;

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getBonusType() {
            return bonusType;
        }

        public void setBonusType(String bonusType) {
            this.bonusType = bonusType;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getFloatLoss() {
            return floatLoss;
        }

        public void setFloatLoss(BigDecimal floatLoss) {
            this.floatLoss = floatLoss;
        }

        public String getFreezeBlance() {
            return freezeBlance;
        }

        public void setFreezeBlance(String freezeBlance) {
            this.freezeBlance = freezeBlance;
        }

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public BigDecimal getMarketValue() {
            return marketValue;
        }

        public void setMarketValue(BigDecimal marketValue) {
            this.marketValue = marketValue;
        }

        public String getNetPrice() {
            return netPrice;
        }

        public void setNetPrice(String netPrice) {
            this.netPrice = netPrice;
        }

        public String getNetPriceDate() {
            return netPriceDate;
        }

        public void setNetPriceDate(String netPriceDate) {
            this.netPriceDate = netPriceDate;
        }

        public String getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "availableBalance='" + availableBalance + '\'' +
                    ", bonusType='" + bonusType + '\'' +
                    ", cashFlag='" + cashFlag + '\'' +
                    ", currency='" + currency + '\'' +
                    ", floatLoss='" + floatLoss + '\'' +
                    ", freezeBlance='" + freezeBlance + '\'' +
                    ", fundCode='" + fundCode + '\'' +
                    ", fundName='" + fundName + '\'' +
                    ", marketValue='" + marketValue + '\'' +
                    ", netPrice='" + netPrice + '\'' +
                    ", netPriceDate='" + netPriceDate + '\'' +
                    ", totalBalance='" + totalBalance + '\'' +
                    '}';
        }
    }

}
