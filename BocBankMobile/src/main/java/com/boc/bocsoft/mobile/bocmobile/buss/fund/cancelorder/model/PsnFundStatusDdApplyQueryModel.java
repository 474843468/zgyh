package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/21.
 */

public class PsnFundStatusDdApplyQueryModel {
    //请求参数封装
    /**
     * 当前页
     */
    private String currentIndex;
    /**
     * 每页显示条数
     */
    private String pageSize;
    /**
     * 基金代码
     */
    private String fundCode;
    /**
     * 定投周期
     */
    private String dtFlag;
    /**
     * 刷新标志
     */
    private String _refresh;


    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getDtFlag() {
        return dtFlag;
    }

    public void setDtFlag(String dtFlag) {
        this.dtFlag = dtFlag;
    }

    public String getRefresh() {
        return _refresh;
    }

    public void setRefresh(String _refresh) {
        this._refresh = _refresh;
    }

    //返回参数封装
    private int recordNumber;
    private List<PsnFundStatusDdApplyQueryModel.ResultListBean> resultList;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<PsnFundStatusDdApplyQueryModel.ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<PsnFundStatusDdApplyQueryModel.ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ResultListBean {
        /**
         * 记录状态
         */
        private String recordStatus;
        /**
         * 基金代码
         */
        private String fundCode;
        /**
         * 基金名称
         */
        private String fundName;
        /**
         * 基金信息对象
         */
        private FundInfoBean fundInfo;
        /**
         * 定投（定赎）序号
         */
        private String fundSeq;
        /**
         * 申请日期
         */
        private String applyDate;
        /**
         * 申请金额/份额
         */
        private String applyAmount;
        /**
         * 定投定赎周期
         */
        private String dtdsFlag;
        /**
         * 交易日期
         */
        private String subDate;
        /**
         * 结束条件
         */
        private String endFlag;
        /**
         * 指定结束日期
         */
        private String endDate;
        /**
         * 指定结束累计次数
         */
        private String endSum;
        /**
         * 指定结束累计金额
         */
        private String endAmt;
        /**
         * 消费方式
         */
        private String feeType;
        /**
         * 交易类型
         */
        private String transType;
        /**
         * 是否连续赎回
         */
        private String sellFlag;

        /**
         * 货币
         */
        private String currency;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getRecordStatus() {
            return recordStatus;
        }

        public void setRecordStatus(String recordStatus) {
            this.recordStatus = recordStatus;
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

        public FundInfoBean getFundInfo() {
            return fundInfo;
        }

        public void setFundInfo(FundInfoBean fundInfo) {
            this.fundInfo = fundInfo;
        }

        public String getFundSeq() {
            return fundSeq;
        }

        public void setFundSeq(String fundSeq) {
            this.fundSeq = fundSeq;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getApplyAmount() {
            return applyAmount;
        }

        public void setApplyAmount(String applyAmount) {
            this.applyAmount = applyAmount;
        }

        public String getDtdsFlag() {
            return dtdsFlag;
        }

        public void setDtdsFlag(String dtdsFlag) {
            this.dtdsFlag = dtdsFlag;
        }

        public String getEndFlag() {
            return endFlag;
        }

        public void setEndFlag(String endFlag) {
            this.endFlag = endFlag;
        }

        public String getSubDate() {
            return subDate;
        }

        public void setSubDate(String subDate) {
            this.subDate = subDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndSum() {
            return endSum;
        }

        public void setEndSum(String endSum) {
            this.endSum = endSum;
        }

        public String getEndAmt() {
            return endAmt;
        }

        public void setEndAmt(String endAmt) {
            this.endAmt = endAmt;
        }

        public String getFeeType() {
            return feeType;
        }

        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getSellFlag() {
            return sellFlag;
        }

        public void setSellFlag(String sellFlag) {
            this.sellFlag = sellFlag;
        }


        public class FundInfoBean {
            private String accountType;
            private double addUpBonus;
            private double addUpNetVal;
            private long alwRdptDat;
            private int applyLowLimit;
            private double balanceLimit;
            private String branch;
            private int cBuyAddLowLmt;
            private int cBuyAddUpLmt;
            private int cBuyLowLmt;
            private int cBuyUpLmt;
            private int cInvtAddLowLmt;
            private int cInvtAddUpLmt;
            private int cInvtLowLmt;
            private int cInvtUpLmt;
            private int cSaleLowLmt;
            private int cSaleUpLmt;
            private boolean canBuy;
            private boolean canChangeIn;
            private boolean canChangeOut;
            private boolean canModBonus;
            private boolean canSale;
            private boolean canScheduleBuy;
            private String cashFlag;
            private String chlCtl;
            private String clrBankCode;
            private String clrBankName;
            private long colEndDate;
            private long colOpenDate;
            private String conversionIn;
            private String conversionOut;
            private String convertFlag;
            private String currency;
            private String custBankCode;
            private String custBankName;
            private double dayIncomeRatio;
            private int dayLimit;
            private String dayNumLimit;
            private String defaultBonus;
            private boolean ebankTransFlag;
            private double feeRate1;
            private double feeRate2;
            private String feeType;
            private String fntype;
            private String fullName;
            private String fundCode;
            private String fundCompanyCode;
            private String fundCompanyName;
            private double fundIncomeRatio;
            private double fundIncomeUnit;
            private String fundName;
            private long fundSetDate;
            private String fundState;
            private String fundToMod;
            private String funderCode;
            private String gIsBonusMod;
            private String gIsBuy;
            private String gIsChgIn;
            private String gIsChgOut;
            private String gIsInvt;
            private String gIsSale;
            private double holdLowCount;
            private int holdQutyLowLimit;
            private String isAccountDT;
            private String isAddDT;
            private String isAddSale;
            private String isAddTA;
            private String isAreaCom;
            private String isAreaFin;
            private String isAreaPvt;
            private String isBonusMod;
            private String isBuy;
            private String isCancelTA;
            private String isChangeIn;
            private String isChangeOut;
            private String isCreditCard;
            private String isDelDT;
            private String isDelTA;
            private String isInvt;
            private String isModDT;
            private String isQuickSale;
            private String isSale;
            private String isSendCIF;
            private String isSftIn;
            private String isSftOut;
            private double netPrice;
            private long netValEndDate;
            private double orderLowLimit;
            private int pBuyAddLowLmt;
            private int pBuyUpLmt;
            private int pByAddUpLmt;
            private int pDTUpLmt;
            private double pInvtAddLowLmt;
            private int pInvtAddUpLmt;
            private int pInvtUpLmt;
            private int pSaleUpLmt;
            private double perLimit;
            private int quickSaleLowLimit;
            private double quickSaleUpLimit;
            private String regBranchCode;
            private String regBranchName;
            private String risklv;
            private int scheduleApplyLowLimit;
            private int sellLowLimit;
            private String sign;
            private int totalLimit;

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public double getAddUpBonus() {
                return addUpBonus;
            }

            public void setAddUpBonus(double addUpBonus) {
                this.addUpBonus = addUpBonus;
            }

            public double getAddUpNetVal() {
                return addUpNetVal;
            }

            public void setAddUpNetVal(double addUpNetVal) {
                this.addUpNetVal = addUpNetVal;
            }

            public long getAlwRdptDat() {
                return alwRdptDat;
            }

            public void setAlwRdptDat(long alwRdptDat) {
                this.alwRdptDat = alwRdptDat;
            }

            public int getApplyLowLimit() {
                return applyLowLimit;
            }

            public void setApplyLowLimit(int applyLowLimit) {
                this.applyLowLimit = applyLowLimit;
            }

            public double getBalanceLimit() {
                return balanceLimit;
            }

            public void setBalanceLimit(double balanceLimit) {
                this.balanceLimit = balanceLimit;
            }

            public String getBranch() {
                return branch;
            }

            public void setBranch(String branch) {
                this.branch = branch;
            }

            public int getCBuyAddLowLmt() {
                return cBuyAddLowLmt;
            }

            public void setCBuyAddLowLmt(int cBuyAddLowLmt) {
                this.cBuyAddLowLmt = cBuyAddLowLmt;
            }

            public int getCBuyAddUpLmt() {
                return cBuyAddUpLmt;
            }

            public void setCBuyAddUpLmt(int cBuyAddUpLmt) {
                this.cBuyAddUpLmt = cBuyAddUpLmt;
            }

            public int getCBuyLowLmt() {
                return cBuyLowLmt;
            }

            public void setCBuyLowLmt(int cBuyLowLmt) {
                this.cBuyLowLmt = cBuyLowLmt;
            }

            public int getCBuyUpLmt() {
                return cBuyUpLmt;
            }

            public void setCBuyUpLmt(int cBuyUpLmt) {
                this.cBuyUpLmt = cBuyUpLmt;
            }

            public int getCInvtAddLowLmt() {
                return cInvtAddLowLmt;
            }

            public void setCInvtAddLowLmt(int cInvtAddLowLmt) {
                this.cInvtAddLowLmt = cInvtAddLowLmt;
            }

            public int getCInvtAddUpLmt() {
                return cInvtAddUpLmt;
            }

            public void setCInvtAddUpLmt(int cInvtAddUpLmt) {
                this.cInvtAddUpLmt = cInvtAddUpLmt;
            }

            public int getCInvtLowLmt() {
                return cInvtLowLmt;
            }

            public void setCInvtLowLmt(int cInvtLowLmt) {
                this.cInvtLowLmt = cInvtLowLmt;
            }

            public int getCInvtUpLmt() {
                return cInvtUpLmt;
            }

            public void setCInvtUpLmt(int cInvtUpLmt) {
                this.cInvtUpLmt = cInvtUpLmt;
            }

            public int getCSaleLowLmt() {
                return cSaleLowLmt;
            }

            public void setCSaleLowLmt(int cSaleLowLmt) {
                this.cSaleLowLmt = cSaleLowLmt;
            }

            public int getCSaleUpLmt() {
                return cSaleUpLmt;
            }

            public void setCSaleUpLmt(int cSaleUpLmt) {
                this.cSaleUpLmt = cSaleUpLmt;
            }

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

            public String getChlCtl() {
                return chlCtl;
            }

            public void setChlCtl(String chlCtl) {
                this.chlCtl = chlCtl;
            }

            public String getClrBankCode() {
                return clrBankCode;
            }

            public void setClrBankCode(String clrBankCode) {
                this.clrBankCode = clrBankCode;
            }

            public String getClrBankName() {
                return clrBankName;
            }

            public void setClrBankName(String clrBankName) {
                this.clrBankName = clrBankName;
            }

            public long getColEndDate() {
                return colEndDate;
            }

            public void setColEndDate(long colEndDate) {
                this.colEndDate = colEndDate;
            }

            public long getColOpenDate() {
                return colOpenDate;
            }

            public void setColOpenDate(long colOpenDate) {
                this.colOpenDate = colOpenDate;
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

            public String getCustBankCode() {
                return custBankCode;
            }

            public void setCustBankCode(String custBankCode) {
                this.custBankCode = custBankCode;
            }

            public String getCustBankName() {
                return custBankName;
            }

            public void setCustBankName(String custBankName) {
                this.custBankName = custBankName;
            }

            public double getDayIncomeRatio() {
                return dayIncomeRatio;
            }

            public void setDayIncomeRatio(double dayIncomeRatio) {
                this.dayIncomeRatio = dayIncomeRatio;
            }

            public int getDayLimit() {
                return dayLimit;
            }

            public void setDayLimit(int dayLimit) {
                this.dayLimit = dayLimit;
            }

            public String getDayNumLimit() {
                return dayNumLimit;
            }

            public void setDayNumLimit(String dayNumLimit) {
                this.dayNumLimit = dayNumLimit;
            }

            public String getDefaultBonus() {
                return defaultBonus;
            }

            public void setDefaultBonus(String defaultBonus) {
                this.defaultBonus = defaultBonus;
            }

            public boolean isEbankTransFlag() {
                return ebankTransFlag;
            }

            public void setEbankTransFlag(boolean ebankTransFlag) {
                this.ebankTransFlag = ebankTransFlag;
            }

            public double getFeeRate1() {
                return feeRate1;
            }

            public void setFeeRate1(double feeRate1) {
                this.feeRate1 = feeRate1;
            }

            public double getFeeRate2() {
                return feeRate2;
            }

            public void setFeeRate2(double feeRate2) {
                this.feeRate2 = feeRate2;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public String getFntype() {
                return fntype;
            }

            public void setFntype(String fntype) {
                this.fntype = fntype;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getFundCode() {
                return fundCode;
            }

            public void setFundCode(String fundCode) {
                this.fundCode = fundCode;
            }

            public String getFundCompanyCode() {
                return fundCompanyCode;
            }

            public void setFundCompanyCode(String fundCompanyCode) {
                this.fundCompanyCode = fundCompanyCode;
            }

            public String getFundCompanyName() {
                return fundCompanyName;
            }

            public void setFundCompanyName(String fundCompanyName) {
                this.fundCompanyName = fundCompanyName;
            }

            public double getFundIncomeRatio() {
                return fundIncomeRatio;
            }

            public void setFundIncomeRatio(double fundIncomeRatio) {
                this.fundIncomeRatio = fundIncomeRatio;
            }

            public double getFundIncomeUnit() {
                return fundIncomeUnit;
            }

            public void setFundIncomeUnit(double fundIncomeUnit) {
                this.fundIncomeUnit = fundIncomeUnit;
            }

            public String getFundName() {
                return fundName;
            }

            public void setFundName(String fundName) {
                this.fundName = fundName;
            }

            public long getFundSetDate() {
                return fundSetDate;
            }

            public void setFundSetDate(long fundSetDate) {
                this.fundSetDate = fundSetDate;
            }

            public String getFundState() {
                return fundState;
            }

            public void setFundState(String fundState) {
                this.fundState = fundState;
            }

            public String getFundToMod() {
                return fundToMod;
            }

            public void setFundToMod(String fundToMod) {
                this.fundToMod = fundToMod;
            }

            public String getFunderCode() {
                return funderCode;
            }

            public void setFunderCode(String funderCode) {
                this.funderCode = funderCode;
            }

            public String getGIsBonusMod() {
                return gIsBonusMod;
            }

            public void setGIsBonusMod(String gIsBonusMod) {
                this.gIsBonusMod = gIsBonusMod;
            }

            public String getGIsBuy() {
                return gIsBuy;
            }

            public void setGIsBuy(String gIsBuy) {
                this.gIsBuy = gIsBuy;
            }

            public String getGIsChgIn() {
                return gIsChgIn;
            }

            public void setGIsChgIn(String gIsChgIn) {
                this.gIsChgIn = gIsChgIn;
            }

            public String getGIsChgOut() {
                return gIsChgOut;
            }

            public void setGIsChgOut(String gIsChgOut) {
                this.gIsChgOut = gIsChgOut;
            }

            public String getGIsInvt() {
                return gIsInvt;
            }

            public void setGIsInvt(String gIsInvt) {
                this.gIsInvt = gIsInvt;
            }

            public String getGIsSale() {
                return gIsSale;
            }

            public void setGIsSale(String gIsSale) {
                this.gIsSale = gIsSale;
            }

            public double getHoldLowCount() {
                return holdLowCount;
            }

            public void setHoldLowCount(double holdLowCount) {
                this.holdLowCount = holdLowCount;
            }

            public int getHoldQutyLowLimit() {
                return holdQutyLowLimit;
            }

            public void setHoldQutyLowLimit(int holdQutyLowLimit) {
                this.holdQutyLowLimit = holdQutyLowLimit;
            }

            public String getIsAccountDT() {
                return isAccountDT;
            }

            public void setIsAccountDT(String isAccountDT) {
                this.isAccountDT = isAccountDT;
            }

            public String getIsAddDT() {
                return isAddDT;
            }

            public void setIsAddDT(String isAddDT) {
                this.isAddDT = isAddDT;
            }

            public String getIsAddSale() {
                return isAddSale;
            }

            public void setIsAddSale(String isAddSale) {
                this.isAddSale = isAddSale;
            }

            public String getIsAddTA() {
                return isAddTA;
            }

            public void setIsAddTA(String isAddTA) {
                this.isAddTA = isAddTA;
            }

            public String getIsAreaCom() {
                return isAreaCom;
            }

            public void setIsAreaCom(String isAreaCom) {
                this.isAreaCom = isAreaCom;
            }

            public String getIsAreaFin() {
                return isAreaFin;
            }

            public void setIsAreaFin(String isAreaFin) {
                this.isAreaFin = isAreaFin;
            }

            public String getIsAreaPvt() {
                return isAreaPvt;
            }

            public void setIsAreaPvt(String isAreaPvt) {
                this.isAreaPvt = isAreaPvt;
            }

            public String getIsBonusMod() {
                return isBonusMod;
            }

            public void setIsBonusMod(String isBonusMod) {
                this.isBonusMod = isBonusMod;
            }

            public String getIsBuy() {
                return isBuy;
            }

            public void setIsBuy(String isBuy) {
                this.isBuy = isBuy;
            }

            public String getIsCancelTA() {
                return isCancelTA;
            }

            public void setIsCancelTA(String isCancelTA) {
                this.isCancelTA = isCancelTA;
            }

            public String getIsChangeIn() {
                return isChangeIn;
            }

            public void setIsChangeIn(String isChangeIn) {
                this.isChangeIn = isChangeIn;
            }

            public String getIsChangeOut() {
                return isChangeOut;
            }

            public void setIsChangeOut(String isChangeOut) {
                this.isChangeOut = isChangeOut;
            }

            public String getIsCreditCard() {
                return isCreditCard;
            }

            public void setIsCreditCard(String isCreditCard) {
                this.isCreditCard = isCreditCard;
            }

            public String getIsDelDT() {
                return isDelDT;
            }

            public void setIsDelDT(String isDelDT) {
                this.isDelDT = isDelDT;
            }

            public String getIsDelTA() {
                return isDelTA;
            }

            public void setIsDelTA(String isDelTA) {
                this.isDelTA = isDelTA;
            }

            public String getIsInvt() {
                return isInvt;
            }

            public void setIsInvt(String isInvt) {
                this.isInvt = isInvt;
            }

            public String getIsModDT() {
                return isModDT;
            }

            public void setIsModDT(String isModDT) {
                this.isModDT = isModDT;
            }

            public String getIsQuickSale() {
                return isQuickSale;
            }

            public void setIsQuickSale(String isQuickSale) {
                this.isQuickSale = isQuickSale;
            }

            public String getIsSale() {
                return isSale;
            }

            public void setIsSale(String isSale) {
                this.isSale = isSale;
            }

            public String getIsSendCIF() {
                return isSendCIF;
            }

            public void setIsSendCIF(String isSendCIF) {
                this.isSendCIF = isSendCIF;
            }

            public String getIsSftIn() {
                return isSftIn;
            }

            public void setIsSftIn(String isSftIn) {
                this.isSftIn = isSftIn;
            }

            public String getIsSftOut() {
                return isSftOut;
            }

            public void setIsSftOut(String isSftOut) {
                this.isSftOut = isSftOut;
            }

            public double getNetPrice() {
                return netPrice;
            }

            public void setNetPrice(double netPrice) {
                this.netPrice = netPrice;
            }

            public long getNetValEndDate() {
                return netValEndDate;
            }

            public void setNetValEndDate(long netValEndDate) {
                this.netValEndDate = netValEndDate;
            }

            public double getOrderLowLimit() {
                return orderLowLimit;
            }

            public void setOrderLowLimit(double orderLowLimit) {
                this.orderLowLimit = orderLowLimit;
            }

            public int getPBuyAddLowLmt() {
                return pBuyAddLowLmt;
            }

            public void setPBuyAddLowLmt(int pBuyAddLowLmt) {
                this.pBuyAddLowLmt = pBuyAddLowLmt;
            }

            public int getPBuyUpLmt() {
                return pBuyUpLmt;
            }

            public void setPBuyUpLmt(int pBuyUpLmt) {
                this.pBuyUpLmt = pBuyUpLmt;
            }

            public int getPByAddUpLmt() {
                return pByAddUpLmt;
            }

            public void setPByAddUpLmt(int pByAddUpLmt) {
                this.pByAddUpLmt = pByAddUpLmt;
            }

            public int getPDTUpLmt() {
                return pDTUpLmt;
            }

            public void setPDTUpLmt(int pDTUpLmt) {
                this.pDTUpLmt = pDTUpLmt;
            }

            public double getPInvtAddLowLmt() {
                return pInvtAddLowLmt;
            }

            public void setPInvtAddLowLmt(double pInvtAddLowLmt) {
                this.pInvtAddLowLmt = pInvtAddLowLmt;
            }

            public int getPInvtAddUpLmt() {
                return pInvtAddUpLmt;
            }

            public void setPInvtAddUpLmt(int pInvtAddUpLmt) {
                this.pInvtAddUpLmt = pInvtAddUpLmt;
            }

            public int getPInvtUpLmt() {
                return pInvtUpLmt;
            }

            public void setPInvtUpLmt(int pInvtUpLmt) {
                this.pInvtUpLmt = pInvtUpLmt;
            }

            public int getPSaleUpLmt() {
                return pSaleUpLmt;
            }

            public void setPSaleUpLmt(int pSaleUpLmt) {
                this.pSaleUpLmt = pSaleUpLmt;
            }

            public double getPerLimit() {
                return perLimit;
            }

            public void setPerLimit(double perLimit) {
                this.perLimit = perLimit;
            }

            public int getQuickSaleLowLimit() {
                return quickSaleLowLimit;
            }

            public void setQuickSaleLowLimit(int quickSaleLowLimit) {
                this.quickSaleLowLimit = quickSaleLowLimit;
            }

            public double getQuickSaleUpLimit() {
                return quickSaleUpLimit;
            }

            public void setQuickSaleUpLimit(double quickSaleUpLimit) {
                this.quickSaleUpLimit = quickSaleUpLimit;
            }

            public String getRegBranchCode() {
                return regBranchCode;
            }

            public void setRegBranchCode(String regBranchCode) {
                this.regBranchCode = regBranchCode;
            }

            public String getRegBranchName() {
                return regBranchName;
            }

            public void setRegBranchName(String regBranchName) {
                this.regBranchName = regBranchName;
            }

            public String getRisklv() {
                return risklv;
            }

            public void setRisklv(String risklv) {
                this.risklv = risklv;
            }

            public int getScheduleApplyLowLimit() {
                return scheduleApplyLowLimit;
            }

            public void setScheduleApplyLowLimit(int scheduleApplyLowLimit) {
                this.scheduleApplyLowLimit = scheduleApplyLowLimit;
            }

            public int getSellLowLimit() {
                return sellLowLimit;
            }

            public void setSellLowLimit(int sellLowLimit) {
                this.sellLowLimit = sellLowLimit;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public int getTotalLimit() {
                return totalLimit;
            }

            public void setTotalLimit(int totalLimit) {
                this.totalLimit = totalLimit;
            }
        }


    }


}
