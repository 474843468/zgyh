package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/19.
 * 053基金对账单持仓查询—返回参数
 */
public class PsnFundStatementBalanceQueryResult {
    /**
     * cardNo : 622752021111120212
     * certificateCode : 370481195252022020
     * certificateId : 身份证
     * count : 20
     * cusName : 李参
     * endflag : Y
     * fundAccount : 1252154
     * list : [{"availableBalance":"120000.99","bonusType":"1","cashFlag":"2","currency":"001","floatLoss":"130.12","freezeBlance":"20000","fundCode":"001356","fundName":"中银保险","marketValue":"1200","netPrice":"1.0","netPriceDate":"20131201","totalBalance":"1000000"},{"availableBalance":"120000.99","bonusType":"2","cashFlag":"1","currency":"001","currencyCode":"014","floatLoss":"-123.00","freezeBlance":"20000","fundCode":"232523","fundName":"中银主题","marketValue":"1200","netPrice":"1.0","netPriceDate":"20131201","totalBalance":"1000000"},{"availableBalance":"120000.99","bonusType":"0","cashFlag":"1","currency":"001","currencyCode":"014","floatLoss":"199.1","freezeBlance":"20000","fundCode":"2325235","fundName":"中银主题","marketValue":"1200","netPrice":"1.0","netPriceDate":"20131201","totalBalance":"1000000"}]
     * seqnum : 121
     */

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

    public static class ListBean {
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
//    /**客户姓名*/
//    private String cusName;
//    /**证件类型*/
//    private String certificateId;
//    /**证件号码*/
//    private String certificateCode;
//    /**基金交易账号*/
//    private String fundAccount;
//    /**银行卡号*/
//    private String cardNo;
//    /**持仓结果列表*/
//    private List<FundStatementBean> list;
//
//    public String getCusName() {
//        return cusName;
//    }
//
//    public void setCusName(String cusName) {
//        this.cusName = cusName;
//    }
//
//    public String getCertificateId() {
//        return certificateId;
//    }
//
//    public void setCertificateId(String certificateId) {
//        this.certificateId = certificateId;
//    }
//
//    public String getCertificateCode() {
//        return certificateCode;
//    }
//
//    public void setCertificateCode(String certificateCode) {
//        this.certificateCode = certificateCode;
//    }
//
//    public String getFundAccount() {
//        return fundAccount;
//    }
//
//    public void setFundAccount(String fundAccount) {
//        this.fundAccount = fundAccount;
//    }
//
//    public String getCardNo() {
//        return cardNo;
//    }
//
//    public void setCardNo(String cardNo) {
//        this.cardNo = cardNo;
//    }
//
//    public List<FundStatementBean> getList() {
//        return list;
//    }
//
//    public void setList(List<FundStatementBean> list) {
//        this.list = list;
//    }
//
//    @Override
//    public String toString() {
//        return "PsnFundStatementBalanceQueryResult{" +
//                "cusName='" + cusName + '\'' +
//                ", certificateId='" + certificateId + '\'' +
//                ", certificateCode='" + certificateCode + '\'' +
//                ", fundAccount='" + fundAccount + '\'' +
//                ", cardNo='" + cardNo + '\'' +
//                ", list=" + list +
//                '}';
//    }
//
//    public class FundStatementBean {
//
//        /**基金代码*/
//        private String fundCode;
//        /**基金名称*/
//        private String fundName;
//        /**当日总余额*/
//        private String totalBalance;
//        /**当日有效余额*/
//        private String availableBalance;
//        /**当日冻结余额*/
//        private String freezeBlance;
//        /**货币码*/
//        private String currencyCode;
//        /**分红方式*/
//        private String bonusType;
//        /**钞汇标识*/
//        private String cashFlag;
//        /**参考基金净值*/
//        private String netPrice;
//        /**净值日期*/
//        private String netPriceDate;
//        /**参考基金市值*/
//        private String marketValue;
//        /**参考盈亏*/
//        private String floatLoss;
//
//        public String getFundCode() {
//            return fundCode;
//        }
//
//        public void setFundCode(String fundCode) {
//            this.fundCode = fundCode;
//        }
//
//        public String getFundName() {
//            return fundName;
//        }
//
//        public void setFundName(String fundName) {
//            this.fundName = fundName;
//        }
//
//        public String getAvailableBalance() {
//            return availableBalance;
//        }
//
//        public void setAvailableBalance(String availableBalance) {
//            this.availableBalance = availableBalance;
//        }
//
//        public String getTotalBalance() {
//            return totalBalance;
//        }
//
//        public void setTotalBalance(String totalBalance) {
//            this.totalBalance = totalBalance;
//        }
//
//        public String getFreezeBlance() {
//            return freezeBlance;
//        }
//
//        public void setFreezeBlance(String freezeBlance) {
//            this.freezeBlance = freezeBlance;
//        }
//
//        public String getCurrencyCode() {
//            return currencyCode;
//        }
//
//        public void setCurrencyCode(String currencyCode) {
//            this.currencyCode = currencyCode;
//        }
//
//        public String getBonusType() {
//            return bonusType;
//        }
//
//        public void setBonusType(String bonusType) {
//            this.bonusType = bonusType;
//        }
//
//        public String getCashFlag() {
//            return cashFlag;
//        }
//
//        public void setCashFlag(String cashFlag) {
//            this.cashFlag = cashFlag;
//        }
//
//        public String getNetPrice() {
//            return netPrice;
//        }
//
//        public void setNetPrice(String netPrice) {
//            this.netPrice = netPrice;
//        }
//
//        public String getNetPriceDate() {
//            return netPriceDate;
//        }
//
//        public void setNetPriceDate(String netPriceDate) {
//            this.netPriceDate = netPriceDate;
//        }
//
//        public String getMarketValue() {
//            return marketValue;
//        }
//
//        public void setMarketValue(String marketValue) {
//            this.marketValue = marketValue;
//        }
//
//        public String getFloatLoss() {
//            return floatLoss;
//        }
//
//        public void setFloatLoss(String floatLoss) {
//            this.floatLoss = floatLoss;
//        }
//
//        @Override
//        public String toString() {
//            return "FundStatementBean{" +
//                    "fundCode='" + fundCode + '\'' +
//                    ", fundName='" + fundName + '\'' +
//                    ", totalBalance='" + totalBalance + '\'' +
//                    ", availableBalance='" + availableBalance + '\'' +
//                    ", freezeBlance='" + freezeBlance + '\'' +
//                    ", currencyCode='" + currencyCode + '\'' +
//                    ", bonusType='" + bonusType + '\'' +
//                    ", cashFlag='" + cashFlag + '\'' +
//                    ", netPrice='" + netPrice + '\'' +
//                    ", netPriceDate='" + netPriceDate + '\'' +
//                    ", marketValue='" + marketValue + '\'' +
//                    ", floatLoss='" + floatLoss + '\'' +
//                    '}';
//        }
//    }
    
}
