package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery;

import org.threeten.bp.LocalDate;

/**
 * 手机取款 -- 汇出详情查询
 * Created by wangf on 2016/7/5.
 */
public class PsnMobileRemitDetailsQueryResult {

    /**
     * remitNo : 24242424
     * cardNo : 00000102811811890
     * fromName : 张三
     * remitAmount : 100000
     * currencyCode : 001
     * cashRemit : 00
     * payeeName : 李四
     * payeeMobile : 18623232323
     * tranDate : 2014/06/12
     * remark : 借钱
     * remitStatus : OU
     * receiptDate : 2014/07/01
     * agentName : 235
     * agentNum : 2345
     */

    //汇款编号
    private String remitNo;
    //汇出账户
    private String cardNo;
    //汇出户名
    private String fromName;
    //汇款金额
    private String remitAmount;
    //币种
    private String currencyCode;
    //钞汇
    private String cashRemit;
    //收款人姓名
    private String payeeName;
    //收款人手机号
    private String payeeMobile;
    //汇款日期
    private LocalDate tranDate;
    //附言
    private String remark;
    //汇款状态
    private String remitStatus;
    //取款日期
    private LocalDate receiptDate;
    //代理点名称
    private String agentName;
    //代理点编号
    private String agentNum;
    //到期日期
    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(String remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public LocalDate getTranDate() {
        return tranDate;
    }

    public void setTranDate(LocalDate tranDate) {
        this.tranDate = tranDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemitStatus() {
        return remitStatus;
    }

    public void setRemitStatus(String remitStatus) {
        this.remitStatus = remitStatus;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
}
