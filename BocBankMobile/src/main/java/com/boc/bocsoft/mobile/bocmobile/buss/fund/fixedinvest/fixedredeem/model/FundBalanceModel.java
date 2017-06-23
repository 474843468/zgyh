package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model;

import java.util.List;

/**
 * Created by huixiaobo on 2016/12/15.
 * 015接口返回对象
 */
public class FundBalanceModel {
    /**
     * fundBalance : [{"bonusType":"0","currency":{"code":"001","fraction":2,"i18nId":"CNY"},"currentCapitalisation":329.77,"fundCode":"000011","fundInfo":{"accountType":"3","addUpBonus":2.0E-4,"addUpNetVal":1.888,"alwRdptDat":1463328000000,"applyLowLimit":0,"balanceLimit":14.1,"branch":"00000","cBuyAddLowLmt":100000,"cBuyAddUpLmt":0,"cBuyLowLmt":1000000,"cBuyUpLmt":0,"cInvtAddLowLmt":1000000,"cInvtAddUpLmt":0,"cInvtLowLmt":0,"cInvtUpLmt":0,"cSaleLowLmt":0,"cSaleUpLmt":0,"canBuy":true,"canChangeIn":true,"canChangeOut":true,"canModBonus":true,"canSale":true,"canScheduleBuy":false,"cashFlag":"CAS","chlCtl":"012345678","clrBankCode":"004","clrBankName":"中国银行","colEndDate":1312128000000,"colOpenDate":1284307200000,"conversionIn":"NY","conversionOut":"NY","convertFlag":"是","currency":"001","custBankCode":"004","custBankName":"中国银行","dayIncomeRatio":0.1,"dayLimit":50000,"dayNumLimit":"500","defaultBonus":"1","ebankTransFlag":true,"feeRate1":0.144,"feeRate2":0.35,"feeType":"1","fntype":"09","fullName":"国泰中基金","fundCode":"000011","fundCompanyCode":"50010000","fundCompanyName":"国泰基金管理公司","fundIncomeRatio":0.02,"fundIncomeUnit":0.002,"fundName":"ssssssssssss","fundSetDate":1316102400000,"fundState":"0","fundToMod":"1","funderCode":"50010000","gIsBonusMod":"N","gIsBuy":"N","gIsChgIn":"N","gIsChgOut":"N","gIsInvt":"N","gIsSale":"N","holdLowCount":111.9,"holdQutyLowLimit":14,"isAccountDT":"N","isAddDT":"N","isAddSale":"Y","isAddTA":"Y","isAreaCom":"Y","isAreaFin":"Y","isAreaPvt":"Y","isBonusMod":"Y","isBuy":"N","isCancelTA":"Y","isChangeIn":"Y","isChangeOut":"Y","isCreditCard":"Y","isDelDT":"N","isDelTA":"Y","isInvt":"Y","isModDT":"N","isQuickSale":"Y","isSale":"Y","isSendCIF":"Y","isSftIn":"Y","isSftOut":"Y","netPrice":1.5555,"netValEndDate":1442678400000,"orderLowLimit":999999.99,"pBuyAddLowLmt":100000,"pBuyUpLmt":0,"pByAddUpLmt":0,"pDTUpLmt":0,"pInvtAddLowLmt":999999.99,"pInvtAddUpLmt":0,"pInvtUpLmt":0,"pSaleUpLmt":0,"perLimit":14.1,"quickSaleLowLimit":13,"quickSaleUpLimit":13.56,"regBranchCode":"02","regBranchName":"国泰基金管理公司","risklv":"4","scheduleApplyLowLimit":100000,"sellLowLimit":0,"sign":"N","totalLimit":5},"mktVal":"1111","netVal":12.11,"nvDate":1391270400000,"totalAvailableBalance":212.34,"totalBalance":212,"totalFrozenBalance":212.34}]
     * summation : 329.77
     */

    private String summation;
    /**
     * bonusType : 0
     * currency : {"code":"001","fraction":2,"i18nId":"CNY"}
     * currentCapitalisation : 329.77
     * fundCode : 000011
     * fundInfo : {"accountType":"3","addUpBonus":2.0E-4,"addUpNetVal":1.888,"alwRdptDat":1463328000000,"applyLowLimit":0,"balanceLimit":14.1,"branch":"00000","cBuyAddLowLmt":100000,"cBuyAddUpLmt":0,"cBuyLowLmt":1000000,"cBuyUpLmt":0,"cInvtAddLowLmt":1000000,"cInvtAddUpLmt":0,"cInvtLowLmt":0,"cInvtUpLmt":0,"cSaleLowLmt":0,"cSaleUpLmt":0,"canBuy":true,"canChangeIn":true,"canChangeOut":true,"canModBonus":true,"canSale":true,"canScheduleBuy":false,"cashFlag":"CAS","chlCtl":"012345678","clrBankCode":"004","clrBankName":"中国银行","colEndDate":1312128000000,"colOpenDate":1284307200000,"conversionIn":"NY","conversionOut":"NY","convertFlag":"是","currency":"001","custBankCode":"004","custBankName":"中国银行","dayIncomeRatio":0.1,"dayLimit":50000,"dayNumLimit":"500","defaultBonus":"1","ebankTransFlag":true,"feeRate1":0.144,"feeRate2":0.35,"feeType":"1","fntype":"09","fullName":"国泰中基金","fundCode":"000011","fundCompanyCode":"50010000","fundCompanyName":"国泰基金管理公司","fundIncomeRatio":0.02,"fundIncomeUnit":0.002,"fundName":"ssssssssssss","fundSetDate":1316102400000,"fundState":"0","fundToMod":"1","funderCode":"50010000","gIsBonusMod":"N","gIsBuy":"N","gIsChgIn":"N","gIsChgOut":"N","gIsInvt":"N","gIsSale":"N","holdLowCount":111.9,"holdQutyLowLimit":14,"isAccountDT":"N","isAddDT":"N","isAddSale":"Y","isAddTA":"Y","isAreaCom":"Y","isAreaFin":"Y","isAreaPvt":"Y","isBonusMod":"Y","isBuy":"N","isCancelTA":"Y","isChangeIn":"Y","isChangeOut":"Y","isCreditCard":"Y","isDelDT":"N","isDelTA":"Y","isInvt":"Y","isModDT":"N","isQuickSale":"Y","isSale":"Y","isSendCIF":"Y","isSftIn":"Y","isSftOut":"Y","netPrice":1.5555,"netValEndDate":1442678400000,"orderLowLimit":999999.99,"pBuyAddLowLmt":100000,"pBuyUpLmt":0,"pByAddUpLmt":0,"pDTUpLmt":0,"pInvtAddLowLmt":999999.99,"pInvtAddUpLmt":0,"pInvtUpLmt":0,"pSaleUpLmt":0,"perLimit":14.1,"quickSaleLowLimit":13,"quickSaleUpLimit":13.56,"regBranchCode":"02","regBranchName":"国泰基金管理公司","risklv":"4","scheduleApplyLowLimit":100000,"sellLowLimit":0,"sign":"N","totalLimit":5}
     * mktVal : 1111
     * netVal : 12.11
     * nvDate : 1391270400000
     * totalAvailableBalance : 212.34
     * totalBalance : 212.0
     * totalFrozenBalance : 212.34
     */

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

    public static class FundBalanceBean {
        private String bonusType;
        /**
         * code : 001
         * fraction : 2
         * i18nId : CNY
         */

        private CurrencyBean currency;
        private String currentCapitalisation;
        private String fundCode;
        /**
         * accountType : 3
         * addUpBonus : 2.0E-4
         * addUpNetVal : 1.888
         * alwRdptDat : 1463328000000
         * applyLowLimit : 0
         * balanceLimit : 14.1
         * branch : 00000
         * cBuyAddLowLmt : 100000
         * cBuyAddUpLmt : 0
         * cBuyLowLmt : 1000000
         * cBuyUpLmt : 0
         * cInvtAddLowLmt : 1000000
         * cInvtAddUpLmt : 0
         * cInvtLowLmt : 0
         * cInvtUpLmt : 0
         * cSaleLowLmt : 0
         * cSaleUpLmt : 0
         * canBuy : true
         * canChangeIn : true
         * canChangeOut : true
         * canModBonus : true
         * canSale : true
         * canScheduleBuy : false
         * cashFlag : CAS
         * chlCtl : 012345678
         * clrBankCode : 004
         * clrBankName : 中国银行
         * colEndDate : 1312128000000
         * colOpenDate : 1284307200000
         * conversionIn : NY
         * conversionOut : NY
         * convertFlag : 是
         * currency : 001
         * custBankCode : 004
         * custBankName : 中国银行
         * dayIncomeRatio : 0.1
         * dayLimit : 50000
         * dayNumLimit : 500
         * defaultBonus : 1
         * ebankTransFlag : true
         * feeRate1 : 0.144
         * feeRate2 : 0.35
         * feeType : 1
         * fntype : 09
         * fullName : 国泰中基金
         * fundCode : 000011
         * fundCompanyCode : 50010000
         * fundCompanyName : 国泰基金管理公司
         * fundIncomeRatio : 0.02
         * fundIncomeUnit : 0.002
         * fundName : ssssssssssss
         * fundSetDate : 1316102400000
         * fundState : 0
         * fundToMod : 1
         * funderCode : 50010000
         * gIsBonusMod : N
         * gIsBuy : N
         * gIsChgIn : N
         * gIsChgOut : N
         * gIsInvt : N
         * gIsSale : N
         * holdLowCount : 111.9
         * holdQutyLowLimit : 14
         * isAccountDT : N
         * isAddDT : N
         * isAddSale : Y
         * isAddTA : Y
         * isAreaCom : Y
         * isAreaFin : Y
         * isAreaPvt : Y
         * isBonusMod : Y
         * isBuy : N
         * isCancelTA : Y
         * isChangeIn : Y
         * isChangeOut : Y
         * isCreditCard : Y
         * isDelDT : N
         * isDelTA : Y
         * isInvt : Y
         * isModDT : N
         * isQuickSale : Y
         * isSale : Y
         * isSendCIF : Y
         * isSftIn : Y
         * isSftOut : Y
         * netPrice : 1.5555
         * netValEndDate : 1442678400000
         * orderLowLimit : 999999.99
         * pBuyAddLowLmt : 100000
         * pBuyUpLmt : 0
         * pByAddUpLmt : 0
         * pDTUpLmt : 0
         * pInvtAddLowLmt : 999999.99
         * pInvtAddUpLmt : 0
         * pInvtUpLmt : 0
         * pSaleUpLmt : 0
         * perLimit : 14.1
         * quickSaleLowLimit : 13
         * quickSaleUpLimit : 13.56
         * regBranchCode : 02
         * regBranchName : 国泰基金管理公司
         * risklv : 4
         * scheduleApplyLowLimit : 100000
         * sellLowLimit : 0
         * sign : N
         * totalLimit : 5
         */

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

        public CurrencyBean getCurrency() {
            return currency;
        }

        public void setCurrency(CurrencyBean currency) {
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

        public FundInfoBean getFundInfo() {
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
            private String accountType;
            private String addUpBonus;
            private String addUpNetVal;
            private long alwRdptDat;
            private int applyLowLimit;
            private String balanceLimit;
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
            private String dayIncomeRatio;
            private int dayLimit;
            private String dayNumLimit;
            private String defaultBonus;
            private boolean ebankTransFlag;
            private String feeRate1;
            private String feeRate2;
            private String feeType;
            private String fntype;
            private String fullName;
            private String fundCode;
            private String fundCompanyCode;
            private String fundCompanyName;
            private String fundIncomeRatio;
            private String fundIncomeUnit;
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
            private String netPrice;
            private long netValEndDate;
            private String orderLowLimit;
            private int pBuyAddLowLmt;
            private int pBuyUpLmt;
            private int pByAddUpLmt;
            private int pDTUpLmt;
            private String pInvtAddLowLmt;
            private int pInvtAddUpLmt;
            private int pInvtUpLmt;
            private int pSaleUpLmt;
            private String perLimit;
            private int quickSaleLowLimit;
            private String quickSaleUpLimit;
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

            public String getAddUpBonus() {
                return addUpBonus;
            }

            public void setAddUpBonus(String addUpBonus) {
                this.addUpBonus = addUpBonus;
            }

            public String getAddUpNetVal() {
                return addUpNetVal;
            }

            public void setAddUpNetVal(String addUpNetVal) {
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

            public String getDayIncomeRatio() {
                return dayIncomeRatio;
            }

            public void setDayIncomeRatio(String dayIncomeRatio) {
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

            public String getHoldLowCount() {
                return holdLowCount;
            }

            public void setHoldLowCount(String holdLowCount) {
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

            public String getPInvtAddLowLmt() {
                return pInvtAddLowLmt;
            }

            public void setPInvtAddLowLmt(String pInvtAddLowLmt) {
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

            public String getPerLimit() {
                return perLimit;
            }

            public void setPerLimit(String perLimit) {
                this.perLimit = perLimit;
            }

            public int getQuickSaleLowLimit() {
                return quickSaleLowLimit;
            }

            public void setQuickSaleLowLimit(int quickSaleLowLimit) {
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
