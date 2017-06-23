package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
 * PsnXpadProductBalanceQuery  响应Model
 * Created by yx on 2016-9-10 16:08:20.
 */
public class PsnXpadProductBalanceQueryResult implements Serializable {

    private String sellPrice;

    private String xpadAccount;

    private String bancAccount;

    private String prodCode;

    private String prodName;

    private String curCode;

    private String yearlyRR;

    private String prodBegin;

    private String prodEnd;

    private String holdingQuantity;

    private String availableQuantity;

    private String canRedeem;

    private String canPartlyRedeem;

    private String canChangeBonusMode;

    private String currentBonusMode;

    private String lowestHoldQuantity;

    private String redeemStartingAmount;

    private String cashRemit;

    private String progressionflag;

    private String bancAccountKey;

    private String productKind;

    private String expProfit;

    private String price;

    private String priceDate;

    private String expAmt;

    private String termType;

    private String canAddBuy;

    private String standardPro;

    private String canAgreementMange;

    private String productTerm;

    private String canAssignDate;

    private String shareValue;

    private String currPeriod;

    private String totalPeriod;

    private String canQuantityExchange;

    private String yearlyRRMax;

    private String tranSeq;

    private String issueType;

    public void setSellPrice(String sellPrice){
        this.sellPrice = sellPrice;
    }
    public String getSellPrice(){
        return this.sellPrice;
    }
    public void setXpadAccount(String xpadAccount){
        this.xpadAccount = xpadAccount;
    }
    public String getXpadAccount(){
        return this.xpadAccount;
    }
    public void setBancAccount(String bancAccount){
        this.bancAccount = bancAccount;
    }
    public String getBancAccount(){
        return this.bancAccount;
    }
    public void setProdCode(String prodCode){
        this.prodCode = prodCode;
    }
    public String getProdCode(){
        return this.prodCode;
    }
    public void setProdName(String prodName){
        this.prodName = prodName;
    }
    public String getProdName(){
        return this.prodName;
    }
    public void setCurCode(String curCode){
        this.curCode = curCode;
    }
    public String getCurCode(){
        return this.curCode;
    }
    public void setYearlyRR(String yearlyRR){
        this.yearlyRR = yearlyRR;
    }
    public String getYearlyRR(){
        return this.yearlyRR;
    }
    public void setProdBegin(String prodBegin){
        this.prodBegin = prodBegin;
    }
    public String getProdBegin(){
        return this.prodBegin;
    }
    public void setProdEnd(String prodEnd){
        this.prodEnd = prodEnd;
    }
    public String getProdEnd(){
        return this.prodEnd;
    }
    public void setHoldingQuantity(String holdingQuantity){
        this.holdingQuantity = holdingQuantity;
    }
    public String getHoldingQuantity(){
        return this.holdingQuantity;
    }
    public void setAvailableQuantity(String availableQuantity){
        this.availableQuantity = availableQuantity;
    }
    public String getAvailableQuantity(){
        return this.availableQuantity;
    }
    public void setCanRedeem(String canRedeem){
        this.canRedeem = canRedeem;
    }
    public String getCanRedeem(){
        return this.canRedeem;
    }
    public void setCanPartlyRedeem(String canPartlyRedeem){
        this.canPartlyRedeem = canPartlyRedeem;
    }
    public String getCanPartlyRedeem(){
        return this.canPartlyRedeem;
    }
    public void setCanChangeBonusMode(String canChangeBonusMode){
        this.canChangeBonusMode = canChangeBonusMode;
    }
    public String getCanChangeBonusMode(){
        return this.canChangeBonusMode;
    }
    public void setCurrentBonusMode(String currentBonusMode){
        this.currentBonusMode = currentBonusMode;
    }
    public String getCurrentBonusMode(){
        return this.currentBonusMode;
    }
    public void setLowestHoldQuantity(String lowestHoldQuantity){
        this.lowestHoldQuantity = lowestHoldQuantity;
    }
    public String getLowestHoldQuantity(){
        return this.lowestHoldQuantity;
    }
    public void setRedeemStartingAmount(String redeemStartingAmount){
        this.redeemStartingAmount = redeemStartingAmount;
    }
    public String getRedeemStartingAmount(){
        return this.redeemStartingAmount;
    }
    public void setCashRemit(String cashRemit){
        this.cashRemit = cashRemit;
    }
    public String getCashRemit(){
        return this.cashRemit;
    }
    public void setProgressionflag(String progressionflag){
        this.progressionflag = progressionflag;
    }
    public String getProgressionflag(){
        return this.progressionflag;
    }
    public void setBancAccountKey(String bancAccountKey){
        this.bancAccountKey = bancAccountKey;
    }
    public String getBancAccountKey(){
        return this.bancAccountKey;
    }
    public void setProductKind(String productKind){
        this.productKind = productKind;
    }
    public String getProductKind(){
        return this.productKind;
    }
    public void setExpProfit(String expProfit){
        this.expProfit = expProfit;
    }
    public String getExpProfit(){
        return this.expProfit;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return this.price;
    }
    public void setPriceDate(String priceDate){
        this.priceDate = priceDate;
    }
    public String getPriceDate(){
        return this.priceDate;
    }
    public void setExpAmt(String expAmt){
        this.expAmt = expAmt;
    }
    public String getExpAmt(){
        return this.expAmt;
    }
    public void setTermType(String termType){
        this.termType = termType;
    }
    public String getTermType(){
        return this.termType;
    }
    public void setCanAddBuy(String canAddBuy){
        this.canAddBuy = canAddBuy;
    }
    public String getCanAddBuy(){
        return this.canAddBuy;
    }
    public void setStandardPro(String standardPro){
        this.standardPro = standardPro;
    }
    public String getStandardPro(){
        return this.standardPro;
    }
    public void setCanAgreementMange(String canAgreementMange){
        this.canAgreementMange = canAgreementMange;
    }
    public String getCanAgreementMange(){
        return this.canAgreementMange;
    }
    public void setProductTerm(String productTerm){
        this.productTerm = productTerm;
    }
    public String getProductTerm(){
        return this.productTerm;
    }
    public void setCanAssignDate(String canAssignDate){
        this.canAssignDate = canAssignDate;
    }
    public String getCanAssignDate(){
        return this.canAssignDate;
    }
    public void setShareValue(String shareValue){
        this.shareValue = shareValue;
    }
    public String getShareValue(){
        return this.shareValue;
    }
    public void setCurrPeriod(String currPeriod){
        this.currPeriod = currPeriod;
    }
    public String getCurrPeriod(){
        return this.currPeriod;
    }
    public void setTotalPeriod(String totalPeriod){
        this.totalPeriod = totalPeriod;
    }
    public String getTotalPeriod(){
        return this.totalPeriod;
    }
    public void setCanQuantityExchange(String canQuantityExchange){
        this.canQuantityExchange = canQuantityExchange;
    }
    public String getCanQuantityExchange(){
        return this.canQuantityExchange;
    }
    public void setYearlyRRMax(String yearlyRRMax){
        this.yearlyRRMax = yearlyRRMax;
    }
    public String getYearlyRRMax(){
        return this.yearlyRRMax;
    }
    public void setTranSeq(String tranSeq){
        this.tranSeq = tranSeq;
    }
    public String getTranSeq(){
        return this.tranSeq;
    }
    public void setIssueType(String issueType){
        this.issueType = issueType;
    }
    public String getIssueType(){
        return this.issueType;
    }

}
