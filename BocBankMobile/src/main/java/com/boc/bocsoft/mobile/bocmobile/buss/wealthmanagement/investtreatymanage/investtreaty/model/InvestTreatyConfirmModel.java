package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model;

import java.io.Serializable;

/**
 * Created by guokai on 2016/10/12.
 */
public class InvestTreatyConfirmModel implements Serializable{
    private String proId;//	产品代码
    private String proName;//	产品名称
    private String proCur;//	产品币种
    private String agrType;//	协议类型
    private String instType;//	投资方式
    private String agrCode;//	协议编号
    private String agrName;//	协议名称
    private String tradeCode;//	交易方向
    private String periodtotal;//	协议总期数
    private String period;//	协议当前期数
    private String periodAge;//	投资周期
    private String mininsperiod;//	最小投资期数
    private String oneperiod;//	单周期投资期限
    private String periodpur;//	购买周期
    private String firstdatepur;//	下次购买日
    private String periodred;//	赎回周期
    private String firstdatered;//	下次赎回日
    private String rate;//	预计年收益率（%）
    private String ratedetail;//	预计年收益率（%）(最大值)
    private String agrPurstart;//	协议投资/赎回起点金额
    private String firstBreakPromise;//	是否允许首笔扣款违约
    private String amountType;//	投资金额模式
    private String amount;//	单期购买金额
    private String minAmount;//	账户保留余额
    private String maxAmount;//	最大购买金额
    private String unit;//	赎回份额
    private String ununitBalpur;//	无持有份额申购判断
    private String minBalunitAndred;//	最低持有份额与赎回金额判断
    private String buyPeriod;//	客户签约期数
    private String finishperiod;//	客户协议已执行期数
    private String remaindperiod;//	客户协议剩余期数
    private String canUpdate;//	是否可修改
    private String canCancel;//	是否可终止
    private String isbenchmark;//	是否为业绩基准产品
    private String isneedred;//	赎回频率
    private String isneedpur;//	购买频率
    private String productKind;//	产品性质
    private String cashRemit;//	钞汇标识
    private String serialCode;//	产品系列编号
    private String isControl;//	是否不限期
    private String endDate;//	协议到期日
    private String accNo;//	银行帐号
    private String transactionId;//	网银交易流水号
    private String startPeriod;//	开始期数
    private String endPeriod;//	结束期数

    private String custAgrCode;//	客户协议编号
    private String tdsDate;//	交易日期
    private String tdsType;//	交易类型
    private String tdsAmt;//	交易金额
    private String tdsUnit;//	交易份额
    private String tdsState;//	交易状态
    private String memo;//	失败原因

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getTdsDate() {
        return tdsDate;
    }

    public void setTdsDate(String tdsDate) {
        this.tdsDate = tdsDate;
    }

    public String getTdsType() {
        return tdsType;
    }

    public void setTdsType(String tdsType) {
        this.tdsType = tdsType;
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public String getTdsUnit() {
        return tdsUnit;
    }

    public void setTdsUnit(String tdsUnit) {
        this.tdsUnit = tdsUnit;
    }

    public String getTdsState() {
        return tdsState;
    }

    public void setTdsState(String tdsState) {
        this.tdsState = tdsState;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    private String operateDate;//	协议修改日期

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getAgrPurstart() {
        return agrPurstart;
    }

    public void setAgrPurstart(String agrPurstart) {
        this.agrPurstart = agrPurstart;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getBuyPeriod() {
        return buyPeriod;
    }

    public void setBuyPeriod(String buyPeriod) {
        this.buyPeriod = buyPeriod;
    }

    public String getCanCancel() {
        return canCancel;
    }

    public void setCanCancel(String canCancel) {
        this.canCancel = canCancel;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getFinishperiod() {
        return finishperiod;
    }

    public void setFinishperiod(String finishperiod) {
        this.finishperiod = finishperiod;
    }

    public String getFirstBreakPromise() {
        return firstBreakPromise;
    }

    public void setFirstBreakPromise(String firstBreakPromise) {
        this.firstBreakPromise = firstBreakPromise;
    }

    public String getFirstdatepur() {
        return firstdatepur;
    }

    public void setFirstdatepur(String firstdatepur) {
        this.firstdatepur = firstdatepur;
    }

    public String getFirstdatered() {
        return firstdatered;
    }

    public void setFirstdatered(String firstdatered) {
        this.firstdatered = firstdatered;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getIsbenchmark() {
        return isbenchmark;
    }

    public void setIsbenchmark(String isbenchmark) {
        this.isbenchmark = isbenchmark;
    }

    public String getIsneedpur() {
        return isneedpur;
    }

    public void setIsneedpur(String isneedpur) {
        this.isneedpur = isneedpur;
    }

    public String getIsneedred() {
        return isneedred;
    }

    public void setIsneedred(String isneedred) {
        this.isneedred = isneedred;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMinBalunitAndred() {
        return minBalunitAndred;
    }

    public void setMinBalunitAndred(String minBalunitAndred) {
        this.minBalunitAndred = minBalunitAndred;
    }

    public String getMininsperiod() {
        return mininsperiod;
    }

    public void setMininsperiod(String mininsperiod) {
        this.mininsperiod = mininsperiod;
    }

    public String getOneperiod() {
        return oneperiod;
    }

    public void setOneperiod(String oneperiod) {
        this.oneperiod = oneperiod;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodAge() {
        return periodAge;
    }

    public void setPeriodAge(String periodAge) {
        this.periodAge = periodAge;
    }

    public String getPeriodpur() {
        return periodpur;
    }

    public void setPeriodpur(String periodpur) {
        this.periodpur = periodpur;
    }

    public String getPeriodred() {
        return periodred;
    }

    public void setPeriodred(String periodred) {
        this.periodred = periodred;
    }

    public String getPeriodtotal() {
        return periodtotal;
    }

    public void setPeriodtotal(String periodtotal) {
        this.periodtotal = periodtotal;
    }

    public String getProCur() {
        return proCur;
    }

    public void setProCur(String proCur) {
        this.proCur = proCur;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(String ratedetail) {
        this.ratedetail = ratedetail;
    }

    public String getRemaindperiod() {
        return remaindperiod;
    }

    public void setRemaindperiod(String remaindperiod) {
        this.remaindperiod = remaindperiod;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnunitBalpur() {
        return ununitBalpur;
    }

    public void setUnunitBalpur(String ununitBalpur) {
        this.ununitBalpur = ununitBalpur;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }}
