package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance;

import org.threeten.bp.LocalDate;

/**
 * 作者：XieDu
 * 创建时间：2016/9/9 17:52
 * 描述：
 */
public class PledgeFinanceQryResultBean {
    /**
     * 网银交易流水号
     */
    private String transId;
    /**
     * 贷款账号
     * 加星显示，当status为A时，flag为Y时有值
     */
    private String bancAccount;
    /**
     * 本次用款金额
     */
    private String amount;
    /**
     * 贷款期限
     * 单位：月
     */
    private String loanPeriod;
    /**
     * 贷款利率
     */
    private String loanRate;
    /**
     * 还款方式
     * 1：到期一次性还本付息 2：按期还息到期还本
     */
    private String payType;
    /**
     * 贷款到期日
     * 当status为A，flag为Y时，有值
     */
    private LocalDate loanToDate;

    /**
     * 贷款申请状态
     * A:申请成功
     * B:申请失败
     * C:交易不存在
     * D:交易处理中
     */
    private String status;
    /**
     * 错误描述
     * 当status为B，flag为Y时，有值
     */
    private String returnMsg;
    /**
     * 后台系统标识
     * Y：网银数据库 N：RCLMS系统
     */
    private String flag;
    /**
     * 默认几秒执行一次
     */
    private String defaultTimeForXpadLoanTime;

    /**
     * 页面倒计时总时间
     */
    private String waitTimeForXpadLoanTime;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public LocalDate getLoanToDate() {
        return loanToDate;
    }

    public void setLoanToDate(LocalDate loanToDate) {
        this.loanToDate = loanToDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDefaultTimeForXpadLoanTime() {
        return defaultTimeForXpadLoanTime;
    }

    public void setDefaultTimeForXpadLoanTime(String defaultTimeForXpadLoanTime) {
        this.defaultTimeForXpadLoanTime = defaultTimeForXpadLoanTime;
    }

    public String getWaitTimeForXpadLoanTime() {
        return waitTimeForXpadLoanTime;
    }

    public void setWaitTimeForXpadLoanTime(String waitTimeForXpadLoanTime) {
        this.waitTimeForXpadLoanTime = waitTimeForXpadLoanTime;
    }
}
