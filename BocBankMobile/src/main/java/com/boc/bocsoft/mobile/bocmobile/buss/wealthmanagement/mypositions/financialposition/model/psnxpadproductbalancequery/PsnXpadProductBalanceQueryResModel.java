package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery;

import java.io.Serializable;

/**
 * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
 * PsnXpadProductBalanceQuery  请求Model
 * Created by yx on 2016-9-10 15:38:55.
 */
public class PsnXpadProductBalanceQueryResModel implements Serializable {
    /**
     * 赎回价格
     */
    private String sellPrice;
    /**
     *客户理财账户
     */
    private String xpadAccount;
    /**
     *资金账号
     */
    private String bancAccount;
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 产品币种
     * 001：人民币元
     014：美元
     012：英镑
     013：港币
     028: 加拿大元
     029：澳元
     038：欧元
     027：日元

     */
    private String curCode;
    /**
     * 预计年收益率（%）
     */
    private String yearlyRR;
    /**
     * 产品起息日
     */
    private String prodBegin;
    /**
     * 产品到期日
     * 无限开放式产品返回“-1”表示“无限期”
     */
    private String prodEnd;
    /**
     * 持有份额
     */
    private String holdingQuantity;
    /**
     * 可用份额
     */
    private String availableQuantity;
    /**
     * 是否可赎回
     * 0：是
     1：否
     */
    private String canRedeem;
    /**
     * 是否允许部分赎回
     * 0：是
     1：否

     */
    private String canPartlyRedeem;
    /**
     * 是否可修改分红方式
     * 0：否
     1：是

     */
    private String canChangeBonusMode;
    /**
     * 当前分红方式
     * 0：红利再投资、
     1：现金分红

     */
    private String currentBonusMode;
    /**
     * 最低持有份额
     */
    private String lowestHoldQuantity;
    /**
     * 赎回起点金额
     */
    private String redeemStartingAmount;
    /**
     * 钞汇标识
     * 01：钞
     02：汇
     00：人民币

     */
    private String cashRemit;
    /**
     * 是否收益累计产品
     * 0：否
     1：是

     */
    private String progressionflag;
    /**
     * 资金账号缓存标识
     */
    private String bancAccountKey;
    /**
     *
     */
    private String productKind;
    /**
     * 参考收益
     */
    private String expProfit;
    /**
     * 单位净值
     */
    private String price;
    /**
     * 净值日期
     */
    private String priceDate;
    /**
     * 参考市值
     */
    private String expAmt;
    /**
     * 产品期限特性
     * 00-有限期封闭式
     01-有限期半开放式
     02-周期连续
     03-周期不连续
     04-无限开放式
     05-春夏秋冬
     06-其它

     */
    private String termType;
    /**
     * 是否可追加购买
     */
    private String canAddBuy;
    /**
     *是否为业绩基准产品
     * 0：非业绩基准产品
     1：业绩基准-锁定期转低收益
     2：业绩基准-锁定期后入账
     3：业绩基准-锁定期周期滚续
     业绩基准产品允许查看份额明细

     */
    private String standardPro;
    /**
     *是否可投资协议管理
     * 0：不允许
     1：允许

     */
    private String canAgreementMange;
    /**
     * 产品期限
     * 固定期限类产品有效
     */
    private String productTerm;
    /**
     *
     */
    private String canAssignDate;
    /**
     *
     */
    private String shareValue;
    /**
     * 当前期数
     */
    private String currPeriod;
    /**
     * 总期数
     */
    private String totalPeriod;
    /**
     *是否可份额转换	String	业绩基准类产品有效0：是 1：否
     */
    private String canQuantityExchange;
    /**
     *预计年收益率（最大值）	String	不带%号，如果不为0，与yearlyRR字段组成区间
     */
    private String yearlyRRMax;
    /**
     * 交易流水号
     */
    private String tranSeq;
    /**
     *
     */
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
