package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/29.
 * 有效申请 Model
 */
public class ValidinvestModel {


    /**
     * list : [{"applyAmount":300,"applyDate":"2020/05/09","cashFlag":"CAS","currency":"001","dtdsFlag":"0","endAmt":0,"endFlag":"0","endSum":"0","feeType":"1","fundCode":"206011","fundInfo":{"accountType":"4","addUpBonus":0,"addUpNetVal":2.5,"applyLowLimit":100,"balanceLimit":0,"branch":"00000","cBuyAddLowLmt":100,"cBuyAddUpLmt":9.999999999999998E13,"cBuyLowLmt":100,"cBuyUpLmt":9999999999,"cInvtAddLowLmt":100,"cInvtAddUpLmt":9999999999,"cInvtLowLmt":100,"cInvtUpLmt":9999999999,"cSaleLowLmt":0,"cSaleUpLmt":9999999999,"canBuy":true,"canChangeIn":true,"canChangeOut":true,"canModBonus":true,"canSale":true,"canScheduleBuy":true,"cashFlag":"CAS","chargeRate":"1","chlCtl":"012345678A","clrBankCode":"004","clrBankName":"中国银行","colEndDate":1321891200000,"colOpenDate":1319558400000,"conversionIn":"YY","conversionOut":"YY","convertFlag":"是","currency":"001","custBankCode":"005","custBankName":"建设银行","dayIncomeRatio":0.1,"defaultBonus":"1","discount":"网银渠道该产品手续费率折扣情况：金额0-999999.99申购申请折扣8折.金额0-999999.99定期定额申购申请折扣8折.","ebankTransFlag":true,"feeRate1":0.78,"feeRate2":0,"feeType":"1","fntKind":"01","fntype":"08","fullName":"鹏华美国房地产","fundCode":"206011","fundCompanyCode":"50060000","fundCompanyName":"鹏华基金管理有限公司","fundIncomeRatio":1.5,"fundIncomeUnit":0,"fundInfoMdfDate":1435248000000,"fundName":"鹏华美国房地产","fundSetDate":1321977600000,"fundState":"0","fundToMod":"1","funderCode":"50060000","gIsBonusMod":"Y","gIsBuy":"N","gIsChgIn":"Y","gIsChgOut":"Y","gIsInvt":"Y","gIsSale":"Y","holdLowCount":30,"holdQutyLowLimit":0,"indiDayMaxSumBuy":"0","indiDayMaxSumRedeem":"0","isAccountDT":"Y","isAddDT":"Y","isAddSale":"Y","isAddTA":"Y","isAreaCom":"Y","isAreaFin":"Y","isAreaPvt":"Y","isBonusMod":"Y","isBuy":"N","isCancelTA":"Y","isChangeIn":"Y","isChangeOut":"Y","isCreditCard":"Y","isDelDT":"Y","isDelSale":"Y","isDelTA":"Y","isFdyk":"Y","isInvt":"Y","isModDT":"Y","isModSale":"Y","isQuickSale":"N","isSale":"Y","isSendCIF":"Y","isSftIn":"Y","isSftOut":"Y","isShortFund":"Y","mjTyp":"1","netPrice":5,"netValEndDate":1551283200000,"orderLowLimit":100,"pBuyAddLowLmt":100,"pBuyUpLmt":9999999999,"pByAddUpLmt":9.999999999999998E13,"pDTUpLmt":9999999999,"pInvtAddLowLmt":100,"pInvtAddUpLmt":9999999999,"pInvtUpLmt":9999999999,"pSaleUpLmt":9999999999,"quickSaleFlag":"N","quickSaleLowLimit":0,"quickSaleUpLimit":100,"regBranchCode":"06","regBranchName":"鹏华基金管理公司","risklv":"5","scheduleApplyLowLimit":100,"sellLowLimit":0,"sevenDayYield":0.2,"sign":"0","zIsInvt":"1","zIsSale":"1","zIsTby":"0"},"fundName":"鹏华美国房地产","fundSeq":"17029","paymentDate":"01","recordStatus":"0","subDate":"01","transType":"0"}]
     * recordNumber : 1
     */

    private int recordNumber;
    /**
     * applyAmount : 300.0
     * applyDate : 2020/05/09
     * cashFlag : CAS
     * currency : 001
     * dtdsFlag : 0
     * endAmt : 0.0
     * endFlag : 0
     * endSum : 0
     * feeType : 1
     * fundCode : 206011
     * fundInfo : {"accountType":"4","addUpBonus":0,"addUpNetVal":2.5,"applyLowLimit":100,"balanceLimit":0,"branch":"00000","cBuyAddLowLmt":100,"cBuyAddUpLmt":9.999999999999998E13,"cBuyLowLmt":100,"cBuyUpLmt":9999999999,"cInvtAddLowLmt":100,"cInvtAddUpLmt":9999999999,"cInvtLowLmt":100,"cInvtUpLmt":9999999999,"cSaleLowLmt":0,"cSaleUpLmt":9999999999,"canBuy":true,"canChangeIn":true,"canChangeOut":true,"canModBonus":true,"canSale":true,"canScheduleBuy":true,"cashFlag":"CAS","chargeRate":"1","chlCtl":"012345678A","clrBankCode":"004","clrBankName":"中国银行","colEndDate":1321891200000,"colOpenDate":1319558400000,"conversionIn":"YY","conversionOut":"YY","convertFlag":"是","currency":"001","custBankCode":"005","custBankName":"建设银行","dayIncomeRatio":0.1,"defaultBonus":"1","discount":"网银渠道该产品手续费率折扣情况：金额0-999999.99申购申请折扣8折.金额0-999999.99定期定额申购申请折扣8折.","ebankTransFlag":true,"feeRate1":0.78,"feeRate2":0,"feeType":"1","fntKind":"01","fntype":"08","fullName":"鹏华美国房地产","fundCode":"206011","fundCompanyCode":"50060000","fundCompanyName":"鹏华基金管理有限公司","fundIncomeRatio":1.5,"fundIncomeUnit":0,"fundInfoMdfDate":1435248000000,"fundName":"鹏华美国房地产","fundSetDate":1321977600000,"fundState":"0","fundToMod":"1","funderCode":"50060000","gIsBonusMod":"Y","gIsBuy":"N","gIsChgIn":"Y","gIsChgOut":"Y","gIsInvt":"Y","gIsSale":"Y","holdLowCount":30,"holdQutyLowLimit":0,"indiDayMaxSumBuy":"0","indiDayMaxSumRedeem":"0","isAccountDT":"Y","isAddDT":"Y","isAddSale":"Y","isAddTA":"Y","isAreaCom":"Y","isAreaFin":"Y","isAreaPvt":"Y","isBonusMod":"Y","isBuy":"N","isCancelTA":"Y","isChangeIn":"Y","isChangeOut":"Y","isCreditCard":"Y","isDelDT":"Y","isDelSale":"Y","isDelTA":"Y","isFdyk":"Y","isInvt":"Y","isModDT":"Y","isModSale":"Y","isQuickSale":"N","isSale":"Y","isSendCIF":"Y","isSftIn":"Y","isSftOut":"Y","isShortFund":"Y","mjTyp":"1","netPrice":5,"netValEndDate":1551283200000,"orderLowLimit":100,"pBuyAddLowLmt":100,"pBuyUpLmt":9999999999,"pByAddUpLmt":9.999999999999998E13,"pDTUpLmt":9999999999,"pInvtAddLowLmt":100,"pInvtAddUpLmt":9999999999,"pInvtUpLmt":9999999999,"pSaleUpLmt":9999999999,"quickSaleFlag":"N","quickSaleLowLimit":0,"quickSaleUpLimit":100,"regBranchCode":"06","regBranchName":"鹏华基金管理公司","risklv":"5","scheduleApplyLowLimit":100,"sellLowLimit":0,"sevenDayYield":0.2,"sign":"0","zIsInvt":"1","zIsSale":"1","zIsTby":"0"}
     * fundName : 鹏华美国房地产
     * fundSeq : 17029
     * paymentDate : 01
     * recordStatus : 0
     * subDate : 01
     * transType : 0
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

    public static class ListBean implements Serializable {
        private String applyAmount;
        private String applyDate;
        private String cashFlag;
        private String currency;
        private String dtdsFlag;
        private String endAmt;
        private String endDate;
        private String endFlag;
        private String endSum;
        private String feeType;
        private String fundCode;
        /**
         * accountType : 4
         * addUpBonus : 0
         * addUpNetVal : 2.5
         * applyLowLimit : 100
         * balanceLimit : 0
         * branch : 00000
         * cBuyAddLowLmt : 100
         * cBuyAddUpLmt : 9.999999999999998E13
         * cBuyLowLmt : 100
         * cBuyUpLmt : 9999999999
         * cInvtAddLowLmt : 100
         * cInvtAddUpLmt : 9999999999
         * cInvtLowLmt : 100
         * cInvtUpLmt : 9999999999
         * cSaleLowLmt : 0
         * cSaleUpLmt : 9999999999
         * canBuy : true
         * canChangeIn : true
         * canChangeOut : true
         * canModBonus : true
         * canSale : true
         * canScheduleBuy : true
         * cashFlag : CAS
         * chargeRate : 1
         * chlCtl : 012345678A
         * clrBankCode : 004
         * clrBankName : 中国银行
         * colEndDate : 1321891200000
         * colOpenDate : 1319558400000
         * conversionIn : YY
         * conversionOut : YY
         * convertFlag : 是
         * currency : 001
         * custBankCode : 005
         * custBankName : 建设银行
         * dayIncomeRatio : 0.1
         * defaultBonus : 1
         * discount : 网银渠道该产品手续费率折扣情况：金额0-999999.99申购申请折扣8折.金额0-999999.99定期定额申购申请折扣8折.
         * ebankTransFlag : true
         * feeRate1 : 0.78
         * feeRate2 : 0
         * feeType : 1
         * fntKind : 01
         * fntype : 08
         * fullName : 鹏华美国房地产
         * fundCode : 206011
         * fundCompanyCode : 50060000
         * fundCompanyName : 鹏华基金管理有限公司
         * fundIncomeRatio : 1.5
         * fundIncomeUnit : 0
         * fundInfoMdfDate : 1435248000000
         * fundName : 鹏华美国房地产
         * fundSetDate : 1321977600000
         * fundState : 0
         * fundToMod : 1
         * funderCode : 50060000
         * gIsBonusMod : Y
         * gIsBuy : N
         * gIsChgIn : Y
         * gIsChgOut : Y
         * gIsInvt : Y
         * gIsSale : Y
         * holdLowCount : 30
         * holdQutyLowLimit : 0
         * indiDayMaxSumBuy : 0
         * indiDayMaxSumRedeem : 0
         * isAccountDT : Y
         * isAddDT : Y
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
         * isDelDT : Y
         * isDelSale : Y
         * isDelTA : Y
         * isFdyk : Y
         * isInvt : Y
         * isModDT : Y
         * isModSale : Y
         * isQuickSale : N
         * isSale : Y
         * isSendCIF : Y
         * isSftIn : Y
         * isSftOut : Y
         * isShortFund : Y
         * mjTyp : 1
         * netPrice : 5
         * netValEndDate : 1551283200000
         * orderLowLimit : 100
         * pBuyAddLowLmt : 100
         * pBuyUpLmt : 9999999999
         * pByAddUpLmt : 9.999999999999998E13
         * pDTUpLmt : 9999999999
         * pInvtAddLowLmt : 100
         * pInvtAddUpLmt : 9999999999
         * pInvtUpLmt : 9999999999
         * pSaleUpLmt : 9999999999
         * quickSaleFlag : N
         * quickSaleLowLimit : 0
         * quickSaleUpLimit : 100
         * regBranchCode : 06
         * regBranchName : 鹏华基金管理公司
         * risklv : 5
         * scheduleApplyLowLimit : 100
         * sellLowLimit : 0
         * sevenDayYield : 0.2
         * sign : 0
         * zIsInvt : 1
         * zIsSale : 1
         * zIsTby : 0
         */

        private FundInfoBean fundInfo;
        private String fundName;
        private String fundSeq;
        private String paymentDate;
        private String recordStatus;
        private String sellFlag;
        private String subDate;
        private String transType;
        private boolean isInvestUpdate;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getSellFlag() {
            return sellFlag;
        }

        public void setSellFlag(String sellFlag) {
            this.sellFlag = sellFlag;
        }

        public boolean isInvestUpdate() {
            return isInvestUpdate;
        }

        public void setInvestUpdate(boolean investUpdate) {
            isInvestUpdate = investUpdate;
        }

        public String getApplyAmount() {
            return applyAmount;
        }

        public void setApplyAmount(String applyAmount) {
            this.applyAmount = applyAmount;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
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

        public String getDtdsFlag() {
            return dtdsFlag;
        }

        public void setDtdsFlag(String dtdsFlag) {
            this.dtdsFlag = dtdsFlag;
        }

        public String getEndAmt() {
            return endAmt;
        }

        public void setEndAmt(String endAmt) {
            this.endAmt = endAmt;
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

        public String getFundSeq() {
            return fundSeq;
        }

        public void setFundSeq(String fundSeq) {
            this.fundSeq = fundSeq;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getRecordStatus() {
            return recordStatus;
        }

        public void setRecordStatus(String recordStatus) {
            this.recordStatus = recordStatus;
        }

        public String getSubDate() {
            return subDate;
        }

        public void setSubDate(String subDate) {
            this.subDate = subDate;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public static class FundInfoBean {
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

            public String getCBuyAddLowLmt() {
                return cBuyAddLowLmt;
            }

            public void setCBuyAddLowLmt(String cBuyAddLowLmt) {
                this.cBuyAddLowLmt = cBuyAddLowLmt;
            }

            public String getCBuyAddUpLmt() {
                return cBuyAddUpLmt;
            }

            public void setCBuyAddUpLmt(String cBuyAddUpLmt) {
                this.cBuyAddUpLmt = cBuyAddUpLmt;
            }

            public String getCBuyLowLmt() {
                return cBuyLowLmt;
            }

            public void setCBuyLowLmt(String cBuyLowLmt) {
                this.cBuyLowLmt = cBuyLowLmt;
            }

            public String getCBuyUpLmt() {
                return cBuyUpLmt;
            }

            public void setCBuyUpLmt(String cBuyUpLmt) {
                this.cBuyUpLmt = cBuyUpLmt;
            }

            public String getCInvtAddLowLmt() {
                return cInvtAddLowLmt;
            }

            public void setCInvtAddLowLmt(String cInvtAddLowLmt) {
                this.cInvtAddLowLmt = cInvtAddLowLmt;
            }

            public String getCInvtAddUpLmt() {
                return cInvtAddUpLmt;
            }

            public void setCInvtAddUpLmt(String cInvtAddUpLmt) {
                this.cInvtAddUpLmt = cInvtAddUpLmt;
            }

            public String getCInvtLowLmt() {
                return cInvtLowLmt;
            }

            public void setCInvtLowLmt(String cInvtLowLmt) {
                this.cInvtLowLmt = cInvtLowLmt;
            }

            public String getCInvtUpLmt() {
                return cInvtUpLmt;
            }

            public void setCInvtUpLmt(String cInvtUpLmt) {
                this.cInvtUpLmt = cInvtUpLmt;
            }

            public String getCSaleLowLmt() {
                return cSaleLowLmt;
            }

            public void setCSaleLowLmt(String cSaleLowLmt) {
                this.cSaleLowLmt = cSaleLowLmt;
            }

            public String getCSaleUpLmt() {
                return cSaleUpLmt;
            }

            public void setCSaleUpLmt(String cSaleUpLmt) {
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

            public String getPBuyAddLowLmt() {
                return pBuyAddLowLmt;
            }

            public void setPBuyAddLowLmt(String pBuyAddLowLmt) {
                this.pBuyAddLowLmt = pBuyAddLowLmt;
            }

            public String getPBuyUpLmt() {
                return pBuyUpLmt;
            }

            public void setPBuyUpLmt(String pBuyUpLmt) {
                this.pBuyUpLmt = pBuyUpLmt;
            }

            public String getPByAddUpLmt() {
                return pByAddUpLmt;
            }

            public void setPByAddUpLmt(String pByAddUpLmt) {
                this.pByAddUpLmt = pByAddUpLmt;
            }

            public String getPDTUpLmt() {
                return pDTUpLmt;
            }

            public void setPDTUpLmt(String pDTUpLmt) {
                this.pDTUpLmt = pDTUpLmt;
            }

            public String getPInvtAddLowLmt() {
                return pInvtAddLowLmt;
            }

            public void setPInvtAddLowLmt(String pInvtAddLowLmt) {
                this.pInvtAddLowLmt = pInvtAddLowLmt;
            }

            public String getPInvtAddUpLmt() {
                return pInvtAddUpLmt;
            }

            public void setPInvtAddUpLmt(String pInvtAddUpLmt) {
                this.pInvtAddUpLmt = pInvtAddUpLmt;
            }

            public String getPInvtUpLmt() {
                return pInvtUpLmt;
            }

            public void setPInvtUpLmt(String pInvtUpLmt) {
                this.pInvtUpLmt = pInvtUpLmt;
            }

            public String getPSaleUpLmt() {
                return pSaleUpLmt;
            }

            public void setPSaleUpLmt(String pSaleUpLmt) {
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

            public String getZIsInvt() {
                return zIsInvt;
            }

            public void setZIsInvt(String zIsInvt) {
                this.zIsInvt = zIsInvt;
            }

            public String getZIsSale() {
                return zIsSale;
            }

            public void setZIsSale(String zIsSale) {
                this.zIsSale = zIsSale;
            }

            public String getZIsTby() {
                return zIsTby;
            }

            public void setZIsTby(String zIsTby) {
                this.zIsTby = zIsTby;
            }
        }
    }




}
