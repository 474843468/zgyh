package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery;

import java.util.List;

/**
 * Created by huixiabo on 2016/11/18.
 * 058失效定期定额查询
 */
public class PsnScheduledFundUnavailableQueryResult {
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

    @Override
    public String toString() {
        return "PsnScheduledFundUnavailableQueryResult{" +
                "recordNumber=" + recordNumber +
                ", list=" + list +
                '}';
    }

    public static class ListBean {
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

        @Override
        public String toString() {
            return "ListBean{" +
                    "applyDate='" + applyDate + '\'' +
                    ", applyType='" + applyType + '\'' +
                    ", cashFlag='" + cashFlag + '\'' +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", endCount='" + endCount + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", endFlag='" + endFlag + '\'' +
                    ", endSum='" + endSum + '\'' +
                    ", feeType='" + feeType + '\'' +
                    ", fundCode='" + fundCode + '\'' +
                    ", fundInfo=" + fundInfo +
                    ", fundName='" + fundName + '\'' +
                    ", invalidationDate='" + invalidationDate + '\'' +
                    ", ornScheduleNum='" + ornScheduleNum + '\'' +
                    ", sellFlag='" + sellFlag + '\'' +
                    ", status='" + status + '\'' +
                    ", transCount='" + transCount + '\'' +
                    ", transCycle='" + transCycle + '\'' +
                    ", transDate='" + transDate + '\'' +
                    '}';
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

            @Override
            public String toString() {
                return "FundInfoBean{" +
                        "canBuy=" + canBuy +
                        ", canChangeIn=" + canChangeIn +
                        ", canChangeOut=" + canChangeOut +
                        ", canModBonus=" + canModBonus +
                        ", canSale=" + canSale +
                        ", canScheduleBuy=" + canScheduleBuy +
                        ", cashFlag='" + cashFlag + '\'' +
                        ", conversionIn='" + conversionIn + '\'' +
                        ", conversionOut='" + conversionOut + '\'' +
                        ", convertFlag='" + convertFlag + '\'' +
                        ", currency='" + currency + '\'' +
                        ", ebankTransFlag=" + ebankTransFlag +
                        '}';
            }
        }
    }
//    /**记录条数*/
//    private String recordNumber;
//    /**申请日期*/
//    private String applyDate;
//    /**失效日期*/
//    private String invalidationDate;
//    /**申请类别*/
//    private String applyType;
//    /**基金代码*/
//    private String fundCode;
//    /**基金名称*/
//    private String fundName;
//    /**货币代码*/
//    private String currencyCode;
//    /**钞汇标识*/
//    private String cashFlag;
//    /**收费方式*/
//    private String feeType;
//    /**交易金额/份额*/
//    private String transCount;
//    /**交易周期*/
//    private String transCycle;
//    /**交易日期*/
//    private String transDate;
//    /**结束条件*/
//    private String endFlag;
//    /**指定结束日期*/
//    private String endDate;
//    /**指定结束累计次数*/
//    private String endSum;
//    /**指定结束累计份额/金额*/
//    private String endCount;
//    /**连续赎回标志*/
//    private String sellFlag;
//    /**状态*/
//    private String status;
//    /**原定投/定赎序号*/
//    private String ornScheduleNum;
//
//    public String getRecordNumber() {
//        return recordNumber;
//    }
//
//    public void setRecordNumber(String recordNumber) {
//        this.recordNumber = recordNumber;
//    }
//
//    public String getApplyDate() {
//        return applyDate;
//    }
//
//    public void setApplyDate(String applyDate) {
//        this.applyDate = applyDate;
//    }
//
//    public String getInvalidationDate() {
//        return invalidationDate;
//    }
//
//    public void setInvalidationDate(String invalidationDate) {
//        this.invalidationDate = invalidationDate;
//    }
//
//    public String getApplyType() {
//        return applyType;
//    }
//
//    public void setApplyType(String applyType) {
//        this.applyType = applyType;
//    }
//
//    public String getFundCode() {
//        return fundCode;
//    }
//
//    public void setFundCode(String fundCode) {
//        this.fundCode = fundCode;
//    }
//
//    public String getFundName() {
//        return fundName;
//    }
//
//    public void setFundName(String fundName) {
//        this.fundName = fundName;
//    }
//
//    public String getCurrencyCode() {
//        return currencyCode;
//    }
//
//    public void setCurrencyCode(String currencyCode) {
//        this.currencyCode = currencyCode;
//    }
//
//    public String getCashFlag() {
//        return cashFlag;
//    }
//
//    public void setCashFlag(String cashFlag) {
//        this.cashFlag = cashFlag;
//    }
//
//    public String getTransCount() {
//        return transCount;
//    }
//
//    public void setTransCount(String transCount) {
//        this.transCount = transCount;
//    }
//
//    public String getFeeType() {
//        return feeType;
//    }
//
//    public void setFeeType(String feeType) {
//        this.feeType = feeType;
//    }
//
//    public String getTransCycle() {
//        return transCycle;
//    }
//
//    public void setTransCycle(String transCycle) {
//        this.transCycle = transCycle;
//    }
//
//    public String getTransDate() {
//        return transDate;
//    }
//
//    public void setTransDate(String transDate) {
//        this.transDate = transDate;
//    }
//
//    public String getEndFlag() {
//        return endFlag;
//    }
//
//    public void setEndFlag(String endFlag) {
//        this.endFlag = endFlag;
//    }
//
//    public String getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
//
//    public String getEndSum() {
//        return endSum;
//    }
//
//    public void setEndSum(String endSum) {
//        this.endSum = endSum;
//    }
//
//    public String getEndCount() {
//        return endCount;
//    }
//
//    public void setEndCount(String endCount) {
//        this.endCount = endCount;
//    }
//
//    public String getSellFlag() {
//        return sellFlag;
//    }
//
//    public void setSellFlag(String sellFlag) {
//        this.sellFlag = sellFlag;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getOrnScheduleNum() {
//        return ornScheduleNum;
//    }
//
//    public void setOrnScheduleNum(String ornScheduleNum) {
//        this.ornScheduleNum = ornScheduleNum;
//    }
//
//    @Override
//    public String toString() {
//        return "PsnScheduledFundUnavailableQueryResult{" +
//                "recordNumber='" + recordNumber + '\'' +
//                ", applyDate='" + applyDate + '\'' +
//                ", invalidationDate='" + invalidationDate + '\'' +
//                ", applyType='" + applyType + '\'' +
//                ", fundCode='" + fundCode + '\'' +
//                ", fundName='" + fundName + '\'' +
//                ", currencyCode='" + currencyCode + '\'' +
//                ", cashFlag='" + cashFlag + '\'' +
//                ", feeType='" + feeType + '\'' +
//                ", transCount='" + transCount + '\'' +
//                ", transCycle='" + transCycle + '\'' +
//                ", transDate='" + transDate + '\'' +
//                ", endFlag='" + endFlag + '\'' +
//                ", endDate='" + endDate + '\'' +
//                ", endSum='" + endSum + '\'' +
//                ", endCount='" + endCount + '\'' +
//                ", sellFlag='" + sellFlag + '\'' +
//                ", status='" + status + '\'' +
//                ", ornScheduleNum='" + ornScheduleNum + '\'' +
//                '}';
//    }




}
