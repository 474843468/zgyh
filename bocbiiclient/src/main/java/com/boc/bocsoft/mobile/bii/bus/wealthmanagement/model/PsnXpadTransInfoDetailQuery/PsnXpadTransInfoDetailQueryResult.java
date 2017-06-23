package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery;

import org.threeten.bp.LocalDate;

/**
 * 交易状况详细信息查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadTransInfoDetailQueryResult {

    /**
     * cashRemit : 00
     * tranDate : null
     * handlingCost : 0.00
     * prodCode : P603-LJJ-hxy0304-hou
     * tranNum : 0.00
     * tranNetVal : 1.00
     * accountNo : 3181****6332
     * tranType : 00
     * confirmDate : 2022/01/01
     * currency : 001
     * tranStatus : 1
     * contrFee : 0.00
     * prodName : hxy1测试网银类基金后端收费
     * tranAmount : 1210.00
     * yearlyRR : 0.00
     * failReason : -
     */

    // 交易申请日期
    private LocalDate tranDate;
    // 产品名称
    private String prodName;
    // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让 12:份额转换）
    private String tranType;
    // 币种
    private String currency;
    // 成交日期/确认日期（yyyy/MM/dd）
    private LocalDate confirmDate;
    // 成交净值
    private String tranNetVal;
    // 成交份额
    private String tranNum;
    // 成交金额
    private String tranAmount;
    // 手续费用
    private String handlingCost;
    // 业绩报酬
    private String contrFee;
    // 失败原因（失败交易返回）
    private String failReason;
    // 实际年收益率（%）（成功交易返回）
    private String yearlyRR;
    // 银行账号（加星显示）
    private String accountNo;
    // 产品代码
    private String prodCode;
    // 钞汇标识(01：钞 02：汇 00：人民币)
    private String cashRemit;
    // 交易状态（0：委托待处理 1：成功 2：失败 3：已撤销 4：已冲正 5：已赎回）
    private String tranStatus;

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setTranDate(LocalDate tranDate) {
        this.tranDate = tranDate;
    }

    public void setHandlingCost(String handlingCost) {
        this.handlingCost = handlingCost;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setTranNum(String tranNum) {
        this.tranNum = tranNum;
    }

    public void setTranNetVal(String tranNetVal) {
        this.tranNetVal = tranNetVal;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public void setConfirmDate(LocalDate confirmDate) {
        this.confirmDate = confirmDate;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public void setContrFee(String contrFee) {
        this.contrFee = contrFee;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public void setYearlyRR(String yearlyRR) {
        this.yearlyRR = yearlyRR;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public LocalDate getTranDate() {
        return tranDate;
    }

    public String getHandlingCost() {
        return handlingCost;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getTranNum() {
        return tranNum;
    }

    public String getTranNetVal() {
        return tranNetVal;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getTranType() {
        return tranType;
    }

    public LocalDate getConfirmDate() {
        return confirmDate;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public String getContrFee() {
        return contrFee;
    }

    public String getProdName() {
        return prodName;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public String getYearlyRR() {
        return yearlyRR;
    }

    public String getFailReason() {
        return failReason;
    }
}
