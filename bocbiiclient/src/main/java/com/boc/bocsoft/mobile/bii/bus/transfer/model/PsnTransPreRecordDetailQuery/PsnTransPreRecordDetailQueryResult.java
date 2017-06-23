package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery;

import org.threeten.bp.LocalDate;

/**
 * 预约交易详情查询
 * Created by wangf on 2016/7/21.
 */
public class PsnTransPreRecordDetailQueryResult {


    /**
     * allAmount : null
     * amount : 100
     * batSeq : 2673779672
     * channel : 1
     * currency : 001
     * executeAmount : null
     * firstSubmitDate : 2017/10/31
     * furInfo : 测试
     * notExecuteAmount : null
     * payeeAccountName : 周二
     * payeeAccountNumber : 4563513600036772150
     * payeeBankName : 陕西省分行营业部
     * payeeIbknum : 43016
     * payerAccountNumber : 103258201045
     * payerIbknum : 43016
     * paymentDate : 2017/11/01
     * periodicalSeq : null
     * status : A
     * transMode : 1
     * transactionId : 3867926925
     * transferType : 01
     */

    //周期预约总笔数
    private String allAmount;
    //金额
    private String amount;
    //转账批次号
    private String batSeq;
    //交易渠道
    private String channel;
    //币种
    private String currency;
    //周期预约已执行笔数
    private String executeAmount;
    //预约日期
    private LocalDate firstSubmitDate;
    //备注
    private String furInfo;
    //周期预约未执行笔数
    private String notExecuteAmount;
    //收款人姓名
    private String payeeAccountName;
    //转入账户账号
    private String payeeAccountNumber;
    //转入账户开户行
    private String payeeBankName;
    //转入账户省行联行号
    private String payeeIbknum;
    //转出账户账号
    private String payerAccountNumber;
    //转出账户省行联行号
    private String payerIbknum;
    //执行日期
    private LocalDate paymentDate;
    //周期预约交易转账批次之序号
    private String periodicalSeq;
    //
    private String status;
    //预约类型
    private String transMode;
    //网银交易序号
    private String transactionId;
    //转账类型
    private String transferType;

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(String batSeq) {
        this.batSeq = batSeq;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExecuteAmount() {
        return executeAmount;
    }

    public void setExecuteAmount(String executeAmount) {
        this.executeAmount = executeAmount;
    }

    public LocalDate getFirstSubmitDate() {
        return firstSubmitDate;
    }

    public void setFirstSubmitDate(LocalDate firstSubmitDate) {
        this.firstSubmitDate = firstSubmitDate;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getNotExecuteAmount() {
        return notExecuteAmount;
    }

    public void setNotExecuteAmount(String notExecuteAmount) {
        this.notExecuteAmount = notExecuteAmount;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeIbknum() {
        return payeeIbknum;
    }

    public void setPayeeIbknum(String payeeIbknum) {
        this.payeeIbknum = payeeIbknum;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getPayerIbknum() {
        return payerIbknum;
    }

    public void setPayerIbknum(String payerIbknum) {
        this.payerIbknum = payerIbknum;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPeriodicalSeq() {
        return periodicalSeq;
    }

    public void setPeriodicalSeq(String periodicalSeq) {
        this.periodicalSeq = periodicalSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
