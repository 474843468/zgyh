package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 登录前基金行情查询-返回结果
 * Created by lxw on 2016/7/29 0029.
 */
public class PsnFundQueryOutlayResult {


    /**
     * list : [{"applyLowLimit":1000,"canBuy":true,"convertFlag":"是","currency":"001","ebankTransFlag":true,"feeType":"1","fntype":"04","fundCode":"163808","fundCompanyCode":"50400000","fundCompanyName":"中银基金管理有限公司","fundInfoMdfDate":"2011/12/26","fundName":"中银100","fundSetDate":"2011/09/16","fundState":"0","fundToMod":"1","holdLowCount":0,"netPrice":5.5981,"orderLowLimit":1000,"risklv":"4","scheduleApplyLowLimit":200,"sellLowLimit":0,"sign":"N","totalCount":null,"isBuy":"N","isInvt":"Y","isSale":"Y","isChangeIn":"Y","isChangeOut":"Y","isBonusMod":"Y","gIsBuy":"N","gIsInvt":"Y","gIsSale":"Y","gIsChgOut":"Y","gIsChgIn":"Y","gIsBonusMod":"Y","isQuickSale":"Y","canScheduleBuy":true,"conversionIn":"YY","conversionOut":"YY","cashFlag":"CAS","isFloatProfit":"Y","isZisInvt":"N","isZisSale":"N","isDate":"N","isaddSale":"N","isModSale":"N","isDelSale":"N","alwRdptDat":null,"iSAddSale":"N","chargeRate":null,"discount":"网银渠道该产品手续费率折扣情况","dayIncomeRatio":"0","fundIncomeUnit":"0","fundIncomeRatio":"0","endDate":"2016/12/20","buyAddLowLmt":"1000","defaultBonus":"1","addUpNetVal":"0"}]
     * recordNumber : 1
     */

    private int recordNumber;
    /**
     * applyLowLimit : 1000
     * canBuy : true
     * convertFlag : 是
     * currency : 001
     * ebankTransFlag : true
     * feeType : 1
     * fntype : 04
     * fundCode : 163808
     * fundCompanyCode : 50400000
     * fundCompanyName : 中银基金管理有限公司
     * fundInfoMdfDate : 2011/12/26
     * fundName : 中银100
     * fundSetDate : 2011/09/16
     * fundState : 0
     * fundToMod : 1
     * holdLowCount : 0
     * netPrice : 5.5981
     * orderLowLimit : 1000
     * risklv : 4
     * scheduleApplyLowLimit : 200
     * sellLowLimit : 0
     * sign : N
     * totalCount : null
     * isBuy : N
     * isInvt : Y
     * isSale : Y
     * isChangeIn : Y
     * isChangeOut : Y
     * isBonusMod : Y
     * gIsBuy : N
     * gIsInvt : Y
     * gIsSale : Y
     * gIsChgOut : Y
     * gIsChgIn : Y
     * gIsBonusMod : Y
     * isQuickSale : Y
     * canScheduleBuy : true
     * conversionIn : YY
     * conversionOut : YY
     * cashFlag : CAS
     * isFloatProfit : Y
     * isZisInvt : N
     * isZisSale : N
     * isDate : N
     * isaddSale : N
     * isModSale : N
     * isDelSale : N
     * alwRdptDat : null
     * iSAddSale : N
     * chargeRate : null
     * discount : 网银渠道该产品手续费率折扣情况
     * dayIncomeRatio : 0
     * fundIncomeUnit : 0
     * fundIncomeRatio : 0
     * endDate : 2016/12/20
     * buyAddLowLmt : 1000
     * defaultBonus : 1
     * addUpNetVal : 0
     */
    @ListItemType(instantiate = ListBean.class)
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

    public static class ListBean {
        private BigDecimal applyLowLimit;
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
        private BigDecimal netPrice;
        private BigDecimal orderLowLimit;
        private String risklv;
        private BigDecimal scheduleApplyLowLimit;
        private BigDecimal sellLowLimit;
        private String sign;
        private Object totalCount;
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
        private String isQuickSale;
        private boolean canScheduleBuy;
        private String conversionIn;
        private String conversionOut;
        private String cashFlag;
        private String isFloatProfit;
        private String isZisInvt;
        private String isZisSale;
        private String isDate;
        private String isaddSale;
        private String isModSale;
        private String isDelSale;
        private Object alwRdptDat;
        private String iSAddSale;
        private Object chargeRate;
        private String discount;
        private String dayIncomeRatio;
        private String fundIncomeUnit;
        private String fundIncomeRatio;
        private String endDate;
        private String buyAddLowLmt;
        private String defaultBonus;
        private String addUpNetVal;

        public BigDecimal getApplyLowLimit() {
            return applyLowLimit;
        }

        public void setApplyLowLimit(BigDecimal applyLowLimit) {
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

        public BigDecimal getNetPrice() {
            return netPrice;
        }

        public void setNetPrice(BigDecimal netPrice) {
            this.netPrice = netPrice;
        }

        public BigDecimal getOrderLowLimit() {
            return orderLowLimit;
        }

        public void setOrderLowLimit(BigDecimal orderLowLimit) {
            this.orderLowLimit = orderLowLimit;
        }

        public String getRisklv() {
            return risklv;
        }

        public void setRisklv(String risklv) {
            this.risklv = risklv;
        }

        public BigDecimal getScheduleApplyLowLimit() {
            return scheduleApplyLowLimit;
        }

        public void setScheduleApplyLowLimit(BigDecimal scheduleApplyLowLimit) {
            this.scheduleApplyLowLimit = scheduleApplyLowLimit;
        }

        public BigDecimal getSellLowLimit() {
            return sellLowLimit;
        }

        public void setSellLowLimit(BigDecimal sellLowLimit) {
            this.sellLowLimit = sellLowLimit;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public Object getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Object totalCount) {
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

        public String getIsQuickSale() {
            return isQuickSale;
        }

        public void setIsQuickSale(String isQuickSale) {
            this.isQuickSale = isQuickSale;
        }

        public boolean isCanScheduleBuy() {
            return canScheduleBuy;
        }

        public void setCanScheduleBuy(boolean canScheduleBuy) {
            this.canScheduleBuy = canScheduleBuy;
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

        public String getIsDate() {
            return isDate;
        }

        public void setIsDate(String isDate) {
            this.isDate = isDate;
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

        public Object getAlwRdptDat() {
            return alwRdptDat;
        }

        public void setAlwRdptDat(Object alwRdptDat) {
            this.alwRdptDat = alwRdptDat;
        }

        public String getISAddSale() {
            return iSAddSale;
        }

        public void setISAddSale(String iSAddSale) {
            this.iSAddSale = iSAddSale;
        }

        public Object getChargeRate() {
            return chargeRate;
        }

        public void setChargeRate(Object chargeRate) {
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
    }
}
