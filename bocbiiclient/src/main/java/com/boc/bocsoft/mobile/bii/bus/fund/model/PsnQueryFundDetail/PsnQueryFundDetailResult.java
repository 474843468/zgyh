package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail;

import java.util.List;

/**
 * 基金信息查询（查询基金行情）返回参数
 * Created by lxw on 2016/8/4 0004.
 */
public class PsnQueryFundDetailResult {
    
    private String recordNumber;
    /**
     * fntKind : 01
     * isZisTby : 0
     * applyLowLimit : 0
     * canBuy : false
     * convertFlag : 是
     * currency : 001
     * ebankTransFlag : true
     * feeType : 1
     * fntype : 01
     * fundCode : 380001
     * fundCompanyCode : 50400000
     * fundCompanyName : 中银基金管理公司
     * fundInfoMdfDate : 2015/06/26
     * fundName : 中银14天债A
     * fundSetDate : 2012/09/24
     * fundState : 0
     * fundToMod : 1
     * holdLowCount : 0
     * netPrice : 1.5
     * orderLowLimit : 0
     * risklv : 1
     * scheduleApplyLowLimit : 99
     * sellLowLimit : 0
     * sign : 0
     * totalCount : null
     * isBuy : N
     * isInvt : N
     * isSale : Y
     * isChangeIn : Y
     * isChangeOut : Y
     * isBonusMod : Y
     * gIsBuy : N
     * gIsInvt : N
     * gIsSale : Y
     * gIsChgOut : Y
     * gIsChgIn : N
     * gIsBonusMod : Y
     * canScheduleBuy : false
     * isModDT : Y
     * isDelDT : Y
     * conversionIn : NY
     * conversionOut : YY
     * cashFlag : CAS
     * isFloatProfit : Y
     * isZisInvt : 0
     * isZisSale : 2
     * isaddSale : Y
     * isModSale : Y
     * isDelSale : Y
     * alwRdptDat : null
     * iSAddSale : Y
     * chargeRate : 1
     * discount : null
     * dayIncomeRatio : 0
     * fundIncomeUnit : 0
     * fundIncomeRatio : 0.1
     * endDate : 2020/03/03
     * buyAddLowLmt : 0
     * defaultBonus : 2
     * addUpNetVal : 0
     * isQuickSale : N
     * quickSaleUpLimit : 99999999999999
     * quickSaleLowLimit : 0
     * holdQutyLowLimit : 10
     * perLimit : 
     * dayLimit : 
     * dayNumLimit : 
     * isShortFund : N
     * indiDayMaxSumBuy : 50000
     * indiDayMaxSumRedeem : 0
     * sevenDayYield : 0
     * branch : 00000
     */

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
        private String fntKind;
        private String isZisTby;
        private String applyLowLimit;
        private boolean canBuy;
        private String convertFlag;
        private String currency;
        private boolean ebankTransFlag;
        private String feeType;
        private String fntype;
        private String fundCode;
        private String fundCompanyCode;
        private String fundCompanyName;
        private String fundInfoMdfDate;
        private String fundName;
        private String fundSetDate;
        private String fundState;
        private String fundToMod;
        private String holdLowCount;
        private String netPrice;
        private String orderLowLimit;
        private String risklv;
        private String scheduleApplyLowLimit;
        private String sellLowLimit;
        private String sign;
        private String totalCount;
        private String isBuy;
        private String isInvt;
        private String isSale;
        private String isChangeIn;
        private String isChangeOut;
        private String isBonusMod;
        private String gIsBuy;
        private String gIsInvt;
        private String gIsSale;
        private String gIsChgOut;
        private String gIsChgIn;
        private String gIsBonusMod;
        private boolean canScheduleBuy;
        private String isModDT;
        private String isDelDT;
        private String conversionIn;
        private String conversionOut;
        private String cashFlag;
        private String isFloatProfit;
        private String isZisInvt;
        private String isZisSale;
        private String isaddSale;
        private String isModSale;
        private String isDelSale;
        private String alwRdptDat;
        private String iSAddSale;
        private String chargeRate;
        private String discount;
        private String dayIncomeRatio;
        private String fundIncomeUnit;
        private String fundIncomeRatio;
        private String endDate;
        private String buyAddLowLmt;
        private String defaultBonus;
        private String addUpNetVal;
        private String isQuickSale;
        private String quickSaleUpLimit;
        private String quickSaleLowLimit;
        private String holdQutyLowLimit;
        private String perLimit;
        private String dayLimit;
        private String dayNumLimit;
        private String isShortFund;
        private String indiDayMaxSumBuy;
        private String indiDayMaxSumRedeem;
        private String sevenDayYield;
        private String branch;

        public String getFntKind() {
            return fntKind;
        }

        public void setFntKind(String fntKind) {
            this.fntKind = fntKind;
        }

        public String getIsZisTby() {
            return isZisTby;
        }

        public void setIsZisTby(String isZisTby) {
            this.isZisTby = isZisTby;
        }

        public String getApplyLowLimit() {
            return applyLowLimit;
        }

        public void setApplyLowLimit(String applyLowLimit) {
            this.applyLowLimit = applyLowLimit;
        }

        public boolean isCanBuy() {
            return canBuy;
        }

        public void setCanBuy(boolean canBuy) {
            this.canBuy = canBuy;
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

        public String getFundInfoMdfDate() {
            return fundInfoMdfDate;
        }

        public void setFundInfoMdfDate(String fundInfoMdfDate) {
            this.fundInfoMdfDate = fundInfoMdfDate;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getFundSetDate() {
            return fundSetDate;
        }

        public void setFundSetDate(String fundSetDate) {
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

        public String getHoldLowCount() {
            return holdLowCount;
        }

        public void setHoldLowCount(String holdLowCount) {
            this.holdLowCount = holdLowCount;
        }

        public String getNetPrice() {
            return netPrice;
        }

        public void setNetPrice(String netPrice) {
            this.netPrice = netPrice;
        }

        public String getOrderLowLimit() {
            return orderLowLimit;
        }

        public void setOrderLowLimit(String orderLowLimit) {
            this.orderLowLimit = orderLowLimit;
        }

        public String getRisklv() {
            return risklv;
        }

        public void setRisklv(String risklv) {
            this.risklv = risklv;
        }

        public String getScheduleApplyLowLimit() {
            return scheduleApplyLowLimit;
        }

        public void setScheduleApplyLowLimit(String scheduleApplyLowLimit) {
            this.scheduleApplyLowLimit = scheduleApplyLowLimit;
        }

        public String getSellLowLimit() {
            return sellLowLimit;
        }

        public void setSellLowLimit(String sellLowLimit) {
            this.sellLowLimit = sellLowLimit;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getIsBuy() {
            return isBuy;
        }

        public void setIsBuy(String isBuy) {
            this.isBuy = isBuy;
        }

        public String getIsInvt() {
            return isInvt;
        }

        public void setIsInvt(String isInvt) {
            this.isInvt = isInvt;
        }

        public String getIsSale() {
            return isSale;
        }

        public void setIsSale(String isSale) {
            this.isSale = isSale;
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

        public String getIsBonusMod() {
            return isBonusMod;
        }

        public void setIsBonusMod(String isBonusMod) {
            this.isBonusMod = isBonusMod;
        }

        public String getGIsBuy() {
            return gIsBuy;
        }

        public void setGIsBuy(String gIsBuy) {
            this.gIsBuy = gIsBuy;
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

        public String getGIsChgOut() {
            return gIsChgOut;
        }

        public void setGIsChgOut(String gIsChgOut) {
            this.gIsChgOut = gIsChgOut;
        }

        public String getGIsChgIn() {
            return gIsChgIn;
        }

        public void setGIsChgIn(String gIsChgIn) {
            this.gIsChgIn = gIsChgIn;
        }

        public String getGIsBonusMod() {
            return gIsBonusMod;
        }

        public void setGIsBonusMod(String gIsBonusMod) {
            this.gIsBonusMod = gIsBonusMod;
        }

        public boolean isCanScheduleBuy() {
            return canScheduleBuy;
        }

        public void setCanScheduleBuy(boolean canScheduleBuy) {
            this.canScheduleBuy = canScheduleBuy;
        }

        public String getIsModDT() {
            return isModDT;
        }

        public void setIsModDT(String isModDT) {
            this.isModDT = isModDT;
        }

        public String getIsDelDT() {
            return isDelDT;
        }

        public void setIsDelDT(String isDelDT) {
            this.isDelDT = isDelDT;
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

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getIsFloatProfit() {
            return isFloatProfit;
        }

        public void setIsFloatProfit(String isFloatProfit) {
            this.isFloatProfit = isFloatProfit;
        }

        public String getIsZisInvt() {
            return isZisInvt;
        }

        public void setIsZisInvt(String isZisInvt) {
            this.isZisInvt = isZisInvt;
        }

        public String getIsZisSale() {
            return isZisSale;
        }

        public void setIsZisSale(String isZisSale) {
            this.isZisSale = isZisSale;
        }

        public String getIsaddSale() {
            return isaddSale;
        }

        public void setIsaddSale(String isaddSale) {
            this.isaddSale = isaddSale;
        }

        public String getIsModSale() {
            return isModSale;
        }

        public void setIsModSale(String isModSale) {
            this.isModSale = isModSale;
        }

        public String getIsDelSale() {
            return isDelSale;
        }

        public void setIsDelSale(String isDelSale) {
            this.isDelSale = isDelSale;
        }

        public String getAlwRdptDat() {
            return alwRdptDat;
        }

        public void setAlwRdptDat(String alwRdptDat) {
            this.alwRdptDat = alwRdptDat;
        }

        public String getISAddSale() {
            return iSAddSale;
        }

        public void setISAddSale(String iSAddSale) {
            this.iSAddSale = iSAddSale;
        }

        public String getChargeRate() {
            return chargeRate;
        }

        public void setChargeRate(String chargeRate) {
            this.chargeRate = chargeRate;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDayIncomeRatio() {
            return dayIncomeRatio;
        }

        public void setDayIncomeRatio(String dayIncomeRatio) {
            this.dayIncomeRatio = dayIncomeRatio;
        }

        public String getFundIncomeUnit() {
            return fundIncomeUnit;
        }

        public void setFundIncomeUnit(String fundIncomeUnit) {
            this.fundIncomeUnit = fundIncomeUnit;
        }

        public String getFundIncomeRatio() {
            return fundIncomeRatio;
        }

        public void setFundIncomeRatio(String fundIncomeRatio) {
            this.fundIncomeRatio = fundIncomeRatio;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getBuyAddLowLmt() {
            return buyAddLowLmt;
        }

        public void setBuyAddLowLmt(String buyAddLowLmt) {
            this.buyAddLowLmt = buyAddLowLmt;
        }

        public String getDefaultBonus() {
            return defaultBonus;
        }

        public void setDefaultBonus(String defaultBonus) {
            this.defaultBonus = defaultBonus;
        }

        public String getAddUpNetVal() {
            return addUpNetVal;
        }

        public void setAddUpNetVal(String addUpNetVal) {
            this.addUpNetVal = addUpNetVal;
        }

        public String getIsQuickSale() {
            return isQuickSale;
        }

        public void setIsQuickSale(String isQuickSale) {
            this.isQuickSale = isQuickSale;
        }

        public String getQuickSaleUpLimit() {
            return quickSaleUpLimit;
        }

        public void setQuickSaleUpLimit(String quickSaleUpLimit) {
            this.quickSaleUpLimit = quickSaleUpLimit;
        }

        public String getQuickSaleLowLimit() {
            return quickSaleLowLimit;
        }

        public void setQuickSaleLowLimit(String quickSaleLowLimit) {
            this.quickSaleLowLimit = quickSaleLowLimit;
        }

        public String getHoldQutyLowLimit() {
            return holdQutyLowLimit;
        }

        public void setHoldQutyLowLimit(String holdQutyLowLimit) {
            this.holdQutyLowLimit = holdQutyLowLimit;
        }

        public String getPerLimit() {
            return perLimit;
        }

        public void setPerLimit(String perLimit) {
            this.perLimit = perLimit;
        }

        public String getDayLimit() {
            return dayLimit;
        }

        public void setDayLimit(String dayLimit) {
            this.dayLimit = dayLimit;
        }

        public String getDayNumLimit() {
            return dayNumLimit;
        }

        public void setDayNumLimit(String dayNumLimit) {
            this.dayNumLimit = dayNumLimit;
        }

        public String getIsShortFund() {
            return isShortFund;
        }

        public void setIsShortFund(String isShortFund) {
            this.isShortFund = isShortFund;
        }

        public String getIndiDayMaxSumBuy() {
            return indiDayMaxSumBuy;
        }

        public void setIndiDayMaxSumBuy(String indiDayMaxSumBuy) {
            this.indiDayMaxSumBuy = indiDayMaxSumBuy;
        }

        public String getIndiDayMaxSumRedeem() {
            return indiDayMaxSumRedeem;
        }

        public void setIndiDayMaxSumRedeem(String indiDayMaxSumRedeem) {
            this.indiDayMaxSumRedeem = indiDayMaxSumRedeem;
        }

        public String getSevenDayYield() {
            return sevenDayYield;
        }

        public void setSevenDayYield(String sevenDayYield) {
            this.sevenDayYield = sevenDayYield;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }
}
