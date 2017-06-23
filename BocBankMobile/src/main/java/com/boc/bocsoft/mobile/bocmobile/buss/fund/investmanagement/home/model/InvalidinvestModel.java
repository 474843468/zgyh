package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/29.
 * 失效申请model
 */
public class InvalidinvestModel {

    /**
     * list : [{"applyDate":"2014/02/02","applyType":"2","cashFlag":"1","currencyCode":"001","endCount":"1000.00","endDate":"2014/02/03","endFlag":"Y","endSum":"10","feeType":"1","fundCode":"850020","fundInfo":{"canBuy":false,"canChangeIn":false,"canChangeOut":false,"canModBonus":false,"canSale":false,"canScheduleBuy":false,"cashFlag":"1","conversionIn":"nullnull","conversionOut":"nullnull","convertFlag":"否","currency":"001","ebankTransFlag":true},"fundName":"华夏超短","invalidationDate":"2014/02/02","ornScheduleNum":"12345678","sellFlag":"1","status":"1","transCount":"123456789.21","transCycle":"1","transDate":"01"}]
     * recordNumber : 1
     */

    private int recordNumber;
    /**
     * applyDate : 2014/02/02
     * applyType : 2
     * cashFlag : 1
     * currencyCode : 001
     * endCount : 1000.00
     * endDate : 2014/02/03
     * endFlag : Y
     * endSum : 10
     * feeType : 1
     * fundCode : 850020
     * fundInfo : {"canBuy":false,"canChangeIn":false,"canChangeOut":false,"canModBonus":false,"canSale":false,"canScheduleBuy":false,"cashFlag":"1","conversionIn":"nullnull","conversionOut":"nullnull","convertFlag":"否","currency":"001","ebankTransFlag":true}
     * fundName : 华夏超短
     * invalidationDate : 2014/02/02
     * ornScheduleNum : 12345678
     * sellFlag : 1
     * status : 1
     * transCount : 123456789.21
     * transCycle : 1
     * transDate : 01
     */

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private String applyDate;
        private String applyType;
        private String cashFlag;
        private String currencyCode;
        private String endCount;
        private String endDate;
        private String endFlag;
        private String endSum;
        private String feeType;
        private String fundCode;
        /**
         * canBuy : false
         * canChangeIn : false
         * canChangeOut : false
         * canModBonus : false
         * canSale : false
         * canScheduleBuy : false
         * cashFlag : 1
         * conversionIn : nullnull
         * conversionOut : nullnull
         * convertFlag : 否
         * currency : 001
         * ebankTransFlag : true
         */

        private FundInfoBean fundInfo;
        private String fundName;
        private String invalidationDate;
        private String ornScheduleNum;
        private String sellFlag;
        private String status;
        private String transCount;
        private String transCycle;
        private String transDate;

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getApplyType() {
            return applyType;
        }

        public void setApplyType(String applyType) {
            this.applyType = applyType;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getEndCount() {
            return endCount;
        }

        public void setEndCount(String endCount) {
            this.endCount = endCount;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndFlag() {
            return endFlag;
        }

        public void setEndFlag(String endFlag) {
            this.endFlag = endFlag;
        }

        public String getEndSum() {
            return endSum;
        }

        public void setEndSum(String endSum) {
            this.endSum = endSum;
        }

        public String getFeeType() {
            return feeType;
        }

        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public FundInfoBean getFundInfo() {
            return fundInfo;
        }

        public void setFundInfo(FundInfoBean fundInfo) {
            this.fundInfo = fundInfo;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getInvalidationDate() {
            return invalidationDate;
        }

        public void setInvalidationDate(String invalidationDate) {
            this.invalidationDate = invalidationDate;
        }

        public String getOrnScheduleNum() {
            return ornScheduleNum;
        }

        public void setOrnScheduleNum(String ornScheduleNum) {
            this.ornScheduleNum = ornScheduleNum;
        }

        public String getSellFlag() {
            return sellFlag;
        }

        public void setSellFlag(String sellFlag) {
            this.sellFlag = sellFlag;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTransCount() {
            return transCount;
        }

        public void setTransCount(String transCount) {
            this.transCount = transCount;
        }

        public String getTransCycle() {
            return transCycle;
        }

        public void setTransCycle(String transCycle) {
            this.transCycle = transCycle;
        }

        public String getTransDate() {
            return transDate;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public static class FundInfoBean {
            private boolean canBuy;
            private boolean canChangeIn;
            private boolean canChangeOut;
            private boolean canModBonus;
            private boolean canSale;
            private boolean canScheduleBuy;
            private String cashFlag;
            private String conversionIn;
            private String conversionOut;
            private String convertFlag;
            private String currency;
            private boolean ebankTransFlag;

            public boolean isCanBuy() {
                return canBuy;
            }

            public void setCanBuy(boolean canBuy) {
                this.canBuy = canBuy;
            }

            public boolean isCanChangeIn() {
                return canChangeIn;
            }

            public void setCanChangeIn(boolean canChangeIn) {
                this.canChangeIn = canChangeIn;
            }

            public boolean isCanChangeOut() {
                return canChangeOut;
            }

            public void setCanChangeOut(boolean canChangeOut) {
                this.canChangeOut = canChangeOut;
            }

            public boolean isCanModBonus() {
                return canModBonus;
            }

            public void setCanModBonus(boolean canModBonus) {
                this.canModBonus = canModBonus;
            }

            public boolean isCanSale() {
                return canSale;
            }

            public void setCanSale(boolean canSale) {
                this.canSale = canSale;
            }

            public boolean isCanScheduleBuy() {
                return canScheduleBuy;
            }

            public void setCanScheduleBuy(boolean canScheduleBuy) {
                this.canScheduleBuy = canScheduleBuy;
            }

            public String getCashFlag() {
                return cashFlag;
            }

            public void setCashFlag(String cashFlag) {
                this.cashFlag = cashFlag;
            }

            public String getConversionIn() {
                return conversionIn;
            }

            public void setConversionIn(String conversionIn) {
                this.conversionIn = conversionIn;
            }

            public String getConversionOut() {
                return conversionOut;
            }

            public void setConversionOut(String conversionOut) {
                this.conversionOut = conversionOut;
            }

            public String getConvertFlag() {
                return convertFlag;
            }

            public void setConvertFlag(String convertFlag) {
                this.convertFlag = convertFlag;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public boolean isEbankTransFlag() {
                return ebankTransFlag;
            }

            public void setEbankTransFlag(boolean ebankTransFlag) {
                this.ebankTransFlag = ebankTransFlag;
            }
        }
    }
}
