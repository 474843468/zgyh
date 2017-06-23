package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/6.
 */

public class FundPositionModel {
    //上报请求参数
    /**
     * 基金代码
     */
    private String fundCode;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    //返回数据
    private String summation;
    private List<FundBalanceBean> fundBalance;

    public String getSummation() {
        return summation;
    }

    public void setSummation(String summation) {
        this.summation = summation;
    }

    public List<FundBalanceBean> getFundBalance() {
        return fundBalance;
    }

    public void setFundBalance(List<FundBalanceBean> fundBalance) {
        this.fundBalance = fundBalance;
    }

    public static class FundBalanceBean implements Serializable {
        private String bonusType;
        private FundBalanceBean.CurrencyBean currency;
        private String currentCapitalisation;
        private String fundCode;

        private FundInfoBean fundInfo;
        private String mktVal;
        private String netVal;
        private long nvDate;
        private String totalAvailableBalance;
        private String totalBalance;
        private String totalFrozenBalance;

        public String getBonusType() {
            return bonusType;
        }

        public void setBonusType(String bonusType) {
            this.bonusType = bonusType;
        }

        public FundBalanceBean.CurrencyBean getCurrency() {
            return currency;
        }

        public void setCurrency(FundBalanceBean.CurrencyBean currency) {
            this.currency = currency;
        }

        public String getCurrentCapitalisation() {
            return currentCapitalisation;
        }

        public void setCurrentCapitalisation(String currentCapitalisation) {
            this.currentCapitalisation = currentCapitalisation;
        }

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public FundBalanceBean.FundInfoBean getFundInfo() {
            return fundInfo;
        }

        public void setFundInfo(FundInfoBean fundInfo) {
            this.fundInfo = fundInfo;
        }

        public String getMktVal() {
            return mktVal;
        }

        public void setMktVal(String mktVal) {
            this.mktVal = mktVal;
        }

        public String getNetVal() {
            return netVal;
        }

        public void setNetVal(String netVal) {
            this.netVal = netVal;
        }

        public long getNvDate() {
            return nvDate;
        }

        public void setNvDate(long nvDate) {
            this.nvDate = nvDate;
        }

        public String getTotalAvailableBalance() {
            return totalAvailableBalance;
        }

        public void setTotalAvailableBalance(String totalAvailableBalance) {
            this.totalAvailableBalance = totalAvailableBalance;
        }

        public String getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

        public String getTotalFrozenBalance() {
            return totalFrozenBalance;
        }

        public void setTotalFrozenBalance(String totalFrozenBalance) {
            this.totalFrozenBalance = totalFrozenBalance;
        }

        public static class CurrencyBean {
            private String code;
            private int fraction;
            private String i18nId;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getFraction() {
                return fraction;
            }

            public void setFraction(int fraction) {
                this.fraction = fraction;
            }

            public String getI18nId() {
                return i18nId;
            }

            public void setI18nId(String i18nId) {
                this.i18nId = i18nId;
            }


        }

        public static class FundInfoBean {
            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public int getAddUpBonus() {
                return addUpBonus;
            }

            public void setAddUpBonus(int addUpBonus) {
                this.addUpBonus = addUpBonus;
            }

            public String getAddUpNetVal() {
                return addUpNetVal;
            }

            public void setAddUpNetVal(String addUpNetVal) {
                this.addUpNetVal = addUpNetVal;
            }

            public String getApplyLowLimit() {
                return applyLowLimit;
            }

            public void setApplyLowLimit(String applyLowLimit) {
                this.applyLowLimit = applyLowLimit;
            }

            public String getBalanceLimit() {
                return balanceLimit;
            }

            public void setBalanceLimit(String balanceLimit) {
                this.balanceLimit = balanceLimit;
            }

            public String getBranch() {
                return branch;
            }

            public void setBranch(String branch) {
                this.branch = branch;
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

            public String getcBuyAddLowLmt() {
                return cBuyAddLowLmt;
            }

            public void setcBuyAddLowLmt(String cBuyAddLowLmt) {
                this.cBuyAddLowLmt = cBuyAddLowLmt;
            }

            public String getcBuyAddUpLmt() {
                return cBuyAddUpLmt;
            }

            public void setcBuyAddUpLmt(String cBuyAddUpLmt) {
                this.cBuyAddUpLmt = cBuyAddUpLmt;
            }

            public String getcBuyLowLmt() {
                return cBuyLowLmt;
            }

            public void setcBuyLowLmt(String cBuyLowLmt) {
                this.cBuyLowLmt = cBuyLowLmt;
            }

            public String getcBuyUpLmt() {
                return cBuyUpLmt;
            }

            public void setcBuyUpLmt(String cBuyUpLmt) {
                this.cBuyUpLmt = cBuyUpLmt;
            }

            public String getChargeRate() {
                return chargeRate;
            }

            public void setChargeRate(String chargeRate) {
                this.chargeRate = chargeRate;
            }

            public String getChlCtl() {
                return chlCtl;
            }

            public void setChlCtl(String chlCtl) {
                this.chlCtl = chlCtl;
            }

            public String getcInvtAddLowLmt() {
                return cInvtAddLowLmt;
            }

            public void setcInvtAddLowLmt(String cInvtAddLowLmt) {
                this.cInvtAddLowLmt = cInvtAddLowLmt;
            }

            public String getcInvtAddUpLmt() {
                return cInvtAddUpLmt;
            }

            public void setcInvtAddUpLmt(String cInvtAddUpLmt) {
                this.cInvtAddUpLmt = cInvtAddUpLmt;
            }

            public String getcInvtLowLmt() {
                return cInvtLowLmt;
            }

            public void setcInvtLowLmt(String cInvtLowLmt) {
                this.cInvtLowLmt = cInvtLowLmt;
            }

            public String getcInvtUpLmt() {
                return cInvtUpLmt;
            }

            public void setcInvtUpLmt(String cInvtUpLmt) {
                this.cInvtUpLmt = cInvtUpLmt;
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

            public String getcSaleLowLmt() {
                return cSaleLowLmt;
            }

            public void setcSaleLowLmt(String cSaleLowLmt) {
                this.cSaleLowLmt = cSaleLowLmt;
            }

            public String getcSaleUpLmt() {
                return cSaleUpLmt;
            }

            public void setcSaleUpLmt(String cSaleUpLmt) {
                this.cSaleUpLmt = cSaleUpLmt;
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

            public String getDayIncomeRatio() {
                return dayIncomeRatio;
            }

            public void setDayIncomeRatio(String dayIncomeRatio) {
                this.dayIncomeRatio = dayIncomeRatio;
            }

            public String getDefaultBonus() {
                return defaultBonus;
            }

            public void setDefaultBonus(String defaultBonus) {
                this.defaultBonus = defaultBonus;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public boolean isEbankTransFlag() {
                return ebankTransFlag;
            }

            public void setEbankTransFlag(boolean ebankTransFlag) {
                this.ebankTransFlag = ebankTransFlag;
            }

            public String getFeeRate1() {
                return feeRate1;
            }

            public void setFeeRate1(String feeRate1) {
                this.feeRate1 = feeRate1;
            }

            public String getFeeRate2() {
                return feeRate2;
            }

            public void setFeeRate2(String feeRate2) {
                this.feeRate2 = feeRate2;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public String getFntKind() {
                return fntKind;
            }

            public void setFntKind(String fntKind) {
                this.fntKind = fntKind;
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

            public String getFunderCode() {
                return funderCode;
            }

            public void setFunderCode(String funderCode) {
                this.funderCode = funderCode;
            }

            public String getFundIncomeRatio() {
                return fundIncomeRatio;
            }

            public void setFundIncomeRatio(String fundIncomeRatio) {
                this.fundIncomeRatio = fundIncomeRatio;
            }

            public String getFundIncomeUnit() {
                return fundIncomeUnit;
            }

            public void setFundIncomeUnit(String fundIncomeUnit) {
                this.fundIncomeUnit = fundIncomeUnit;
            }

            public long getFundInfoMdfDate() {
                return fundInfoMdfDate;
            }

            public void setFundInfoMdfDate(long fundInfoMdfDate) {
                this.fundInfoMdfDate = fundInfoMdfDate;
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

            public String getgIsBonusMod() {
                return gIsBonusMod;
            }

            public void setgIsBonusMod(String gIsBonusMod) {
                this.gIsBonusMod = gIsBonusMod;
            }

            public String getgIsBuy() {
                return gIsBuy;
            }

            public void setgIsBuy(String gIsBuy) {
                this.gIsBuy = gIsBuy;
            }

            public String getgIsChgIn() {
                return gIsChgIn;
            }

            public void setgIsChgIn(String gIsChgIn) {
                this.gIsChgIn = gIsChgIn;
            }

            public String getgIsChgOut() {
                return gIsChgOut;
            }

            public void setgIsChgOut(String gIsChgOut) {
                this.gIsChgOut = gIsChgOut;
            }

            public String getgIsInvt() {
                return gIsInvt;
            }

            public void setgIsInvt(String gIsInvt) {
                this.gIsInvt = gIsInvt;
            }

            public String getgIsSale() {
                return gIsSale;
            }

            public void setgIsSale(String gIsSale) {
                this.gIsSale = gIsSale;
            }

            public String getHoldLowCount() {
                return holdLowCount;
            }

            public void setHoldLowCount(String holdLowCount) {
                this.holdLowCount = holdLowCount;
            }

            public String getHoldQutyLowLimit() {
                return holdQutyLowLimit;
            }

            public void setHoldQutyLowLimit(String holdQutyLowLimit) {
                this.holdQutyLowLimit = holdQutyLowLimit;
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

            public String getIsDelSale() {
                return isDelSale;
            }

            public void setIsDelSale(String isDelSale) {
                this.isDelSale = isDelSale;
            }

            public String getIsDelTA() {
                return isDelTA;
            }

            public void setIsDelTA(String isDelTA) {
                this.isDelTA = isDelTA;
            }

            public String getIsFdyk() {
                return isFdyk;
            }

            public void setIsFdyk(String isFdyk) {
                this.isFdyk = isFdyk;
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

            public String getIsModSale() {
                return isModSale;
            }

            public void setIsModSale(String isModSale) {
                this.isModSale = isModSale;
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

            public String getIsShortFund() {
                return isShortFund;
            }

            public void setIsShortFund(String isShortFund) {
                this.isShortFund = isShortFund;
            }

            public String getMjTyp() {
                return mjTyp;
            }

            public void setMjTyp(String mjTyp) {
                this.mjTyp = mjTyp;
            }

            public String getNetPrice() {
                return netPrice;
            }

            public void setNetPrice(String netPrice) {
                this.netPrice = netPrice;
            }

            public long getNetValEndDate() {
                return netValEndDate;
            }

            public void setNetValEndDate(long netValEndDate) {
                this.netValEndDate = netValEndDate;
            }

            public String getOrderLowLimit() {
                return orderLowLimit;
            }

            public void setOrderLowLimit(String orderLowLimit) {
                this.orderLowLimit = orderLowLimit;
            }

            public String getpBuyAddLowLmt() {
                return pBuyAddLowLmt;
            }

            public void setpBuyAddLowLmt(String pBuyAddLowLmt) {
                this.pBuyAddLowLmt = pBuyAddLowLmt;
            }

            public String getpBuyUpLmt() {
                return pBuyUpLmt;
            }

            public void setpBuyUpLmt(String pBuyUpLmt) {
                this.pBuyUpLmt = pBuyUpLmt;
            }

            public String getpByAddUpLmt() {
                return pByAddUpLmt;
            }

            public void setpByAddUpLmt(String pByAddUpLmt) {
                this.pByAddUpLmt = pByAddUpLmt;
            }

            public String getpDTUpLmt() {
                return pDTUpLmt;
            }

            public void setpDTUpLmt(String pDTUpLmt) {
                this.pDTUpLmt = pDTUpLmt;
            }

            public String getpInvtAddLowLmt() {
                return pInvtAddLowLmt;
            }

            public void setpInvtAddLowLmt(String pInvtAddLowLmt) {
                this.pInvtAddLowLmt = pInvtAddLowLmt;
            }

            public String getpInvtAddUpLmt() {
                return pInvtAddUpLmt;
            }

            public void setpInvtAddUpLmt(String pInvtAddUpLmt) {
                this.pInvtAddUpLmt = pInvtAddUpLmt;
            }

            public String getpInvtUpLmt() {
                return pInvtUpLmt;
            }

            public void setpInvtUpLmt(String pInvtUpLmt) {
                this.pInvtUpLmt = pInvtUpLmt;
            }

            public String getpSaleUpLmt() {
                return pSaleUpLmt;
            }

            public void setpSaleUpLmt(String pSaleUpLmt) {
                this.pSaleUpLmt = pSaleUpLmt;
            }

            public String getQuickSaleFlag() {
                return quickSaleFlag;
            }

            public void setQuickSaleFlag(String quickSaleFlag) {
                this.quickSaleFlag = quickSaleFlag;
            }

            public String getQuickSaleLowLimit() {
                return quickSaleLowLimit;
            }

            public void setQuickSaleLowLimit(String quickSaleLowLimit) {
                this.quickSaleLowLimit = quickSaleLowLimit;
            }

            public String getQuickSaleUpLimit() {
                return quickSaleUpLimit;
            }

            public void setQuickSaleUpLimit(String quickSaleUpLimit) {
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

            public String getSevenDayYield() {
                return sevenDayYield;
            }

            public void setSevenDayYield(String sevenDayYield) {
                this.sevenDayYield = sevenDayYield;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getzIsInvt() {
                return zIsInvt;
            }

            public void setzIsInvt(String zIsInvt) {
                this.zIsInvt = zIsInvt;
            }

            public String getzIsSale() {
                return zIsSale;
            }

            public void setzIsSale(String zIsSale) {
                this.zIsSale = zIsSale;
            }

            public String getzIsTby() {
                return zIsTby;
            }

            public void setzIsTby(String zIsTby) {
                this.zIsTby = zIsTby;
            }

            private String accountType;
            private int addUpBonus;
            private String addUpNetVal;
            private String applyLowLimit;
            private String balanceLimit;
            private String branch;
            private String cBuyAddLowLmt;
            private String cBuyAddUpLmt;
            private String cBuyLowLmt;
            private String cBuyUpLmt;
            private String cInvtAddLowLmt;
            private String cInvtAddUpLmt;
            private String cInvtLowLmt;
            private String cInvtUpLmt;
            private String cSaleLowLmt;
            private String cSaleUpLmt;
            private boolean canBuy;
            private boolean canChangeIn;
            private boolean canChangeOut;
            private boolean canModBonus;
            private boolean canSale;
            private boolean canScheduleBuy;
            private String cashFlag;
            private String chargeRate;
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
            private String dayIncomeRatio;
            private String defaultBonus;
            private String discount;
            private boolean ebankTransFlag;
            private String feeRate1;
            private String feeRate2;
            private String feeType;
            private String fntKind;
            private String fntype;
            private String fullName;
            private String fundCode;
            private String fundCompanyCode;
            private String fundCompanyName;
            private String fundIncomeRatio;
            private String fundIncomeUnit;
            private long fundInfoMdfDate;
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
            private String holdLowCount;
            private String holdQutyLowLimit;
            private String indiDayMaxSumBuy;
            private String indiDayMaxSumRedeem;
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
            private String isDelSale;
            private String isDelTA;
            private String isFdyk;
            private String isInvt;
            private String isModDT;
            private String isModSale;
            private String isQuickSale;
            private String isSale;
            private String isSendCIF;
            private String isSftIn;
            private String isSftOut;
            private String isShortFund;
            private String mjTyp;
            private String netPrice;
            private long netValEndDate;
            private String orderLowLimit;
            private String pBuyAddLowLmt;
            private String pBuyUpLmt;
            private String pByAddUpLmt;
            private String pDTUpLmt;
            private String pInvtAddLowLmt;
            private String pInvtAddUpLmt;
            private String pInvtUpLmt;
            private String pSaleUpLmt;
            private String quickSaleFlag;
            private String quickSaleLowLimit;
            private String quickSaleUpLimit;
            private String regBranchCode;
            private String regBranchName;
            private String risklv;
            private String scheduleApplyLowLimit;
            private String sellLowLimit;
            private String sevenDayYield;
            private String sign;
            private String zIsInvt;
            private String zIsSale;
            private String zIsTby;


        }

    }
}
